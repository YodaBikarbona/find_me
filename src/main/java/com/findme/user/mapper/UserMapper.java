package com.findme.user.mapper;

import com.findme.user.dao.response.CreatedUserDao;
import com.findme.user.dao.response.ResponseLoginDao;
import com.findme.user.model.UserEntity;
import com.findme.utils.JwtUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final JwtUtil jwtUtil;

    public UserMapper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public CreatedUserDao userEntityToNewUserDao(UserEntity userEntity) {
        return new CreatedUserDao(userEntity.getId(), userEntity.getEmail(), userEntity.getUsername(), userEntity.getPhoneNumber());
    }

    public ResponseLoginDao userEntityToResponseLoginDao(UserEntity userEntity) {
        String accessToken = jwtUtil.generateToken(userEntity, Boolean.TRUE);
        String refreshToken = jwtUtil.generateToken(userEntity, Boolean.FALSE);
        return new ResponseLoginDao(userEntity.getId(), accessToken, refreshToken);
    }
}
