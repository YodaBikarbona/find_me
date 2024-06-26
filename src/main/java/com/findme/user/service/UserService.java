package com.findme.user.service;

import com.findme.exceptions.AuthorizationException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.service.ProfileService;
import com.findme.security.model.SecurityEntity;
import com.findme.security.repository.SecurityRepository;
import com.findme.user.dto.request.RequestLoginDto;
import com.findme.user.dto.request.RequestNewUserDto;
import com.findme.user.dto.response.CredentialsDto;
import com.findme.user.mapper.UserMapper;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import com.findme.utils.ApplicationCtxHolderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final ProfileService profileService;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);

    @Transactional
    public void registerUser(RequestNewUserDto newUserDto) throws InternalServerErrorException {
        try {
            SecurityEntity security = new SecurityEntity(newUserDto.getPassword());
            securityRepository.save(security);
            UserEntity userEntity = new UserEntity(newUserDto.getEmail(), newUserDto.getUsername(), newUserDto.getPhoneNumber(), security);
            userRepository.save(userEntity);
        } catch (Exception ex) {
            logger.error("The user cannot be successfully registered!");
            throw new InternalServerErrorException("Internal Server Error!");
        }

    }

    @Transactional
    public CredentialsDto loginUser(RequestLoginDto requestLoginDto) throws InternalServerErrorException {
        UserEntity user = userRepository.findByUsername(requestLoginDto.username())
                .orElseThrow(() -> new AuthorizationException("Invalid credentials!"));
        matchPasswords(requestLoginDto.password(), user.getSecurity().getPassword());
        return generateNewCredentials(user);
    }

    @Transactional
    public CredentialsDto refreshTokens(long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthorizationException("Invalid credentials!"));
        return generateNewCredentials(user);
    }

    private CredentialsDto generateNewCredentials(UserEntity user) {
        try {
            ProfileEntity profile = null;
            try {
                profile = profileService.getProfile(user.getId());
            } catch (Exception ex) {
                logger.info(String.format("The profile could not be found for the userId %s !", user.getId()));
            }
            user.getSecurity().generateTokenSecret();
            securityRepository.save(user.getSecurity());
            return userMapper.userEntityToResponseCredentialsDto(user, profile);
        } catch (Exception ex) {
            logger.error("The user cannot get new credentials!", ex);
            throw new InternalServerErrorException("Internal Server Error!");
        }
    }


    private void matchPasswords(String rawPassword, String password) throws AuthorizationException {
        if (!encoder.matches(rawPassword, password)) {
            logger.info("The passwords aren't identical!");
            throw new AuthorizationException("Invalid credentials!");
        }
    }

}
