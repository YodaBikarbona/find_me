package com.findme.user.mapper;

import com.findme.user.dto.response.NewUserDto;
import com.findme.user.dto.response.LoginDto;
import com.findme.user.model.UserEntity;
import com.findme.utils.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final JwtUtil jwtUtil;

    public UserMapper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public NewUserDto userEntityToNewUserDao(UserEntity userEntity) {
        return new NewUserDto(userEntity.getId(), userEntity.getEmail(), userEntity.getUsername(), userEntity.getPhoneNumber());
    }

    public LoginDto userEntityToResponseLoginDao(UserEntity userEntity) {
        String accessToken = jwtUtil.generateToken(userEntity, Boolean.TRUE);
        String refreshToken = jwtUtil.generateToken(userEntity, Boolean.FALSE);
        return new LoginDto(userEntity.getId(), accessToken, refreshToken);
    }
}
