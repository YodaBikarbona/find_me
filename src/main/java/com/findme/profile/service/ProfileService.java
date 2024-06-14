package com.findme.profile.service;

import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.profile.dto.request.EditProfileDto;
import com.findme.profile.dto.request.NewProfileDto;
import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.mapper.ProfileMapper;
import com.findme.profile.model.Gender;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.repository.ProfileRepository;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import com.findme.utils.ApplicationCtxHolderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);


    public ProfileDto fetchProfile(long userId) {
        ProfileEntity profile = getProfile(userId);
        return profileMapper.profileEntityToProfileDto(profile);
    }

    @Transactional
    public ProfileDto createNewProfile(NewProfileDto data, long userId) throws ConflictException, InternalServerErrorException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The user doesn't exist!"));
        Optional<ProfileEntity> profile = profileRepository.findByUserId(userId);
        if (profile.isPresent()) {
            logger.info(String.format("The profile already exist for the userId: %s", userId));
            throw new ConflictException("The profile already exist!");
        }
        try {
            ProfileEntity newProfile = new ProfileEntity(data.firstName(), data.lastName(), Gender.findByName(data.gender()), data.birthday(), data.aboutMe(), user);
            profileRepository.save(newProfile);
            return profileMapper.profileEntityToProfileDto(newProfile);
        } catch (Exception ex) {
            logger.error("The profile cannot be successfully created!", ex);
            throw new InternalServerErrorException("Internal Server Error!");
        }
    }

    @Transactional
    public void editProfile(EditProfileDto data, long userId) throws InternalServerErrorException {
        ProfileEntity profile = getProfile(userId);
        checkDuplicateData(profile, data);
        try {
            profile.setFirstName(data.firstName());
            profile.setLastName(data.lastName());
            profile.setAboutMe(data.aboutMe());
            profile.setBirthday(data.birthday());
            profileRepository.save(profile);
        } catch (Exception ex) {
            logger.error("The profile cannot be successfully edited!", ex);
            throw new InternalServerErrorException("Internal Server Error!");
        }

    }

    public ProfileEntity getProfile(long userId) throws NotFoundException {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("The profile doesn't exist!"));
    }

    private void checkDuplicateData(ProfileEntity profile, EditProfileDto data) {
        if (profile.getFirstName().equals(data.firstName())
                && profile.getLastName().equals(data.lastName())
                && profile.getAboutMe().equals(data.aboutMe()) && profile.getBirthday().equals(data.birthday())) {
            logger.info("The data sent in the request is identical as the profile data!");
            throw new ConflictException("Identical data sent!");
        }
    }

}
