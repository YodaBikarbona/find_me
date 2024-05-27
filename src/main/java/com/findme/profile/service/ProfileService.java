package com.findme.profile.service;

import com.findme.exceptions.NotFoundException;
import com.findme.profile.dto.request.RequestNewProfileDto;
import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.mapper.ProfileMapper;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.repository.ProfileRepository;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
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

    @Transactional
    public ProfileDto createNewProfile(RequestNewProfileDto newProfileDto, Long userId) throws BadRequestException {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new BadRequestException("The user doesn't exist!");
        }
        ProfileEntity profile = profileRepository.findByUserId(userId);
        if (profile != null) {
            throw new BadRequestException("The profile already exist!");
        }
        profile = new ProfileEntity(newProfileDto.getFirstName(), newProfileDto.getLastName(), newProfileDto.getGender(), newProfileDto.getBirthday(), newProfileDto.getAboutMe(), user.get());
        profileRepository.save(profile);
        return profileMapper.profileEntityToProfileDto(profile);
    }

    public ProfileDto fetchProfile(Long userId) throws NotFoundException {
        ProfileEntity profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            throw new NotFoundException("The profile doesn't exist!");
        }
        return profileMapper.profileEntityToProfileDto(profile);
    }
}
