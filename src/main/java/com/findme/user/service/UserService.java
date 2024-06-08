package com.findme.user.service;

import com.findme.exceptions.AuthorizationException;
import com.findme.exceptions.BadRequestException;
import com.findme.security.model.SecurityEntity;
import com.findme.security.repository.SecurityRepository;
import com.findme.user.dto.request.RequestLoginDto;
import com.findme.user.dto.request.RequestNewUserDto;
import com.findme.user.dto.response.NewUserDto;
import com.findme.user.dto.response.LoginDto;
import com.findme.user.mapper.UserMapper;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Transactional
    public NewUserDto registerUser(RequestNewUserDto newUserDto) {
        SecurityEntity security = new SecurityEntity(newUserDto.getPassword());
        securityRepository.save(security);
        UserEntity userEntity = new UserEntity(newUserDto.getEmail(), newUserDto.getUsername(), newUserDto.getPhoneNumber(), security);
        userRepository.save(userEntity);
        return userMapper.userEntityToNewUserDao(userEntity);
    }

    @Transactional
    public LoginDto loginUser(RequestLoginDto requestLoginDto) {
        UserEntity user = userRepository.findByUsername(requestLoginDto.getUsername())
                .orElseThrow(() -> new AuthorizationException("Invalid credentials!"));
        matchPasswords(requestLoginDto.getPassword(), user.getSecurity().getPassword());
        user.getSecurity().generateTokenSecret();
        securityRepository.save(user.getSecurity());
        return userMapper.userEntityToResponseLoginDao(user);
    }

    private void matchPasswords(String rawPassword, String password) throws BadRequestException {
        if (!encoder.matches(rawPassword, password)) {
            throw new BadRequestException("Invalid credentials!");
        }
    }

}
