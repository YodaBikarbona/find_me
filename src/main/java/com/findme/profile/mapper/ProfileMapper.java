package com.findme.profile.mapper;

import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.model.ProfileEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileDto profileEntityToProfileDto(ProfileEntity profile) {
        return new ProfileDto(profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getGender(), profile.getBirthday(), profile.getAboutMe());
    }

}
