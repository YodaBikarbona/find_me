package com.findme.profile.service;

import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.profile.dto.request.EditProfileDto;
import com.findme.profile.dto.request.NewProfileDto;
import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.mapper.ProfileMapper;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.repository.ProfileRepository;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }

    public ProfileDto fetchProfile(long userId) {
        ProfileEntity profile = getProfile(userId);
        return profileMapper.profileEntityToProfileDto(profile);
    }

    @Transactional
    public ProfileDto createNewProfile(NewProfileDto data, long userId) throws ConflictException, InternalServerErrorException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("The user doesn't exist!"));
        Optional<ProfileEntity> profile = profileRepository.findByUserId(userId);
        if (profile.isEmpty()) {
            throw new ConflictException("The profile already exist!");
        }
        try {
            ProfileEntity newProfile = new ProfileEntity(data.firstName(), data.lastName(), data.gender(), data.birthday(), data.aboutMe(), user);
            profileRepository.save(newProfile);
            return profileMapper.profileEntityToProfileDto(newProfile);
        } catch (Exception ex) {
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
            throw new InternalServerErrorException("Internal Server Error!");
        }

    }

    private ProfileEntity getProfile(long userId) throws NotFoundException {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("The profile doesn't exist!"));
    }

    private void checkDuplicateData(ProfileEntity profile, EditProfileDto data) {
        if (profile.getFirstName().equals(data.firstName())
                && profile.getLastName().equals(data.lastName())
                && profile.getAboutMe().equals(data.aboutMe()) && profile.getBirthday().equals(data.birthday())) {
            throw new ConflictException("Identical data sent!");
        }
    }

}
