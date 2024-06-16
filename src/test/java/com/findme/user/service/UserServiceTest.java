package com.findme.user.service;

import com.findme.exceptions.AuthorizationException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.security.repository.SecurityRepository;
import com.findme.user.dto.request.RequestLoginDto;
import com.findme.user.dto.request.RequestNewUserDto;
import com.findme.user.dto.response.CredentialsDto;
import com.findme.user.mapper.UserMapper;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({UserService.class, UserMapper.class})
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityRepository securityRepository;

    private RequestNewUserDto mockRequestNewUserDto() {
        return new RequestNewUserDto(
                "test@example.com",
                "testUser",
                "+385950000000",
                "Password1.",
                "Password1."
        );
    }

    private RequestLoginDto mockRequestLoginDto() {
        return new RequestLoginDto("testUser", "Password1.");
    }


    @Test
    @Transactional
    @DisplayName("Register the user successfully.")
    void testRegisterUserSuccess() {
        // Arrange
        RequestNewUserDto requestNewUserDto = mockRequestNewUserDto();

        // Act
        userService.registerUser(requestNewUserDto);

        // Assert
        UserEntity userEntity = userRepository.findByEmail("test@example.com").orElse(null);
        assertNotNull(userEntity);
        assertEquals(requestNewUserDto.getEmail(), userEntity.getEmail());
        assertEquals(requestNewUserDto.getUsername(), userEntity.getUsername());
        assertEquals(requestNewUserDto.getPhoneNumber(), userEntity.getPhoneNumber());
        assertNotNull(userEntity.getId());

    }

    @Test
    @Transactional
    @DisplayName("Fail to register the user due to some exception!")
    void testRegisterUserFail() {
        // Arrange
        RequestNewUserDto requestNewUserDto = new RequestNewUserDto(
                "test@example.com",
                null,
                "+385950000000",
                "Password1.",
                "Password1."
        );

        // Act & Assert
        InternalServerErrorException thrownException = assertThrows(InternalServerErrorException.class, () -> {
            userService.registerUser(requestNewUserDto);
        });

        // Assert
        assertEquals("Internal Server Error!", thrownException.getMessage());

    }

    @Test
    @Transactional
    @DisplayName("Login the user successfully.")
    void testLoginUserSuccess() {
        // Arrange
        RequestNewUserDto requestNewUserDto = mockRequestNewUserDto();
        userService.registerUser(requestNewUserDto);
        RequestLoginDto requestLoginDto = mockRequestLoginDto();

        // Act & Assert
        CredentialsDto credentialsDto = userService.loginUser(requestLoginDto);

        // Assert
        UserEntity userEntity = userRepository.findByUsername(requestLoginDto.username()).orElse(null);
        assertNotNull(userEntity);
        assertEquals(userEntity.getId(), credentialsDto.id());

    }

    @Test
    @Transactional
    @DisplayName("Login failed, the user doesn't exist!")
    void testLoginUserFailUserDoesNotExist() {
        // Arrange
        RequestLoginDto requestLoginDto = mockRequestLoginDto();

        // Act & Assert
        AuthorizationException thrownException = assertThrows(AuthorizationException.class, () -> {
            userService.loginUser(requestLoginDto);
        });

        // Assert
        assertEquals("Invalid credentials!", thrownException.getMessage());

    }

    @Test
    @Transactional
    @DisplayName("Login failed, the password is invalid!")
    void testLoginUserFailInvalidPassword() {
        // Arrange
        RequestNewUserDto requestNewUserDto = mockRequestNewUserDto();
        userService.registerUser(requestNewUserDto);
        RequestLoginDto requestLoginDto = new RequestLoginDto("testUser", "invalidPassword");

        // Act & Assert
        AuthorizationException thrownException = assertThrows(AuthorizationException.class, () -> {
            userService.loginUser(requestLoginDto);
        });

        // Assert
        assertEquals("Invalid credentials!", thrownException.getMessage());

    }

    @Test
    @Transactional
    @DisplayName("The tokens has successfully refreshed.")
    void testRefreshTokensSuccess() {
        // Arrange
        RequestNewUserDto requestNewUserDto = mockRequestNewUserDto();
        userService.registerUser(requestNewUserDto);
        UserEntity registeredUser = userRepository.findByUsername(requestNewUserDto.getUsername()).orElse(null);
        assertNotNull(registeredUser);

        // Act & Assert
        CredentialsDto credentialsDto = userService.refreshTokens(registeredUser.getId());

        // Assert
        UserEntity userEntity = userRepository.findByUsername(requestNewUserDto.getUsername()).orElse(null);
        assertNotNull(userEntity);
        assertEquals(userEntity.getId(), credentialsDto.id());

    }

    @Test
    @Transactional
    @DisplayName("Refresh tokens failed, the user doesn't exist!")
    void testRefreshTokensFail() {
        // Arrange

        // Act & Assert
        AuthorizationException thrownException = assertThrows(AuthorizationException.class, () -> {
            userService.refreshTokens(1);
        });

        // Assert
        assertEquals("Invalid credentials!", thrownException.getMessage());

    }

}