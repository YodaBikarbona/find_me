package com.findme.profile.mapper;

import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.model.ProfileEntity;
import com.findme.profileimage.dto.response.ProfileImageDto;
import com.findme.profileimage.mapper.ProfileImageMapper;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    private final ProfileImageMapper profileImageMapper;

    public ProfileMapper(ProfileImageMapper profileImageMapper) {
        this.profileImageMapper = profileImageMapper;
    }

    public ProfileDto profileEntityToProfileDto(ProfileEntity profile) {
        ProfileImageDto thumbnail = null;
        if (profile.getProfileImage() != null) {
            thumbnail = profileImageMapper.profileImageEntityToProfileImageDto(profile.getProfileImage());
        }

        return ProfileDto.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .gender(profile.getGender())
                .birthday(profile.getBirthday())
                .aboutMe(profile.getAboutMe())
                .thumbnail(thumbnail)
                .build();

    }

}
