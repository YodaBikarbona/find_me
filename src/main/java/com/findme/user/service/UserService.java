package com.findme.user.service;

import com.findme.exceptions.AuthorizationException;
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
import org.apache.coyote.BadRequestException;
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
    public NewUserDto registerUser(RequestNewUserDto newUserDto) throws BadRequestException {
        UserEntity user = userRepository.findByEmail(newUserDto.getEmail());
        if (user != null) {
            throw new BadRequestException("This user already exists!");
        }
        SecurityEntity security = new SecurityEntity(newUserDto.getPassword());
        securityRepository.save(security);
        user = new UserEntity(newUserDto.getEmail(), newUserDto.getUsername(), newUserDto.getPhoneNumber());
        user.setSecurity(security);
        userRepository.save(user);
        return userMapper.userEntityToNewUserDao(user);
    }

    @Transactional
    public LoginDto loginUser(RequestLoginDto requestLoginDto) throws BadRequestException {
        UserEntity user = userRepository.findByUsername(requestLoginDto.getUsername())
                .orElseThrow(() -> new AuthorizationException("Invalid credentials!"));

        if (!encoder.matches(requestLoginDto.getPassword(), user.getSecurity().getPassword())) {
            throw new BadRequestException("Incorrect user or password!");
        }

        user.getSecurity().generateTokenSecret();
        securityRepository.save(user.getSecurity());
        return userMapper.userEntityToResponseLoginDao(user);
    }
}
