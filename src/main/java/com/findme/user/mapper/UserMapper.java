package com.findme.user.mapper;

import com.findme.profile.model.ProfileEntity;
import com.findme.user.dto.response.CredentialsDto;
import com.findme.user.model.UserEntity;
import com.findme.utils.JwtUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    private final JwtUtil jwtUtil;

    public UserMapper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public CredentialsDto userEntityToResponseCredentialsDto(UserEntity userEntity, ProfileEntity profileEntity) {
        String accessToken = jwtUtil.generateToken(userEntity, Boolean.TRUE);
        String refreshToken = jwtUtil.generateToken(userEntity, Boolean.FALSE);
        return new CredentialsDto(
                userEntity.getId(),
                !Objects.isNull(profileEntity) ? profileEntity.getId() : null,
                accessToken,
                refreshToken
        );
    }
}
