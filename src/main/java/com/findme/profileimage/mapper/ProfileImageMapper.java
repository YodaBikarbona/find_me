package com.findme.profileimage.mapper;

import com.findme.profileimage.dto.response.ProfileImageDto;
import com.findme.profileimage.model.ProfileImageEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileImageMapper {

    public ProfileImageDto profileImageEntityToProfileImageDto(ProfileImageEntity image) {
        return new ProfileImageDto(image.getId(), image.getUrl());
    }

}
