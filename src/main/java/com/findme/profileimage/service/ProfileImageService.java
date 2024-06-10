package com.findme.profileimage.service;

import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.repository.ProfileRepository;
import com.findme.profileimage.dto.request.RequestNewProfileImageDto;
import com.findme.profileimage.dto.response.ProfileImageDto;
import com.findme.profileimage.mapper.ProfileImageMapper;
import com.findme.profileimage.model.ProfileImageEntity;
import com.findme.profileimage.repository.ProfileImageRepository;
import com.findme.utils.GCSUtil;
import com.findme.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageMapper profileImageMapper;
    private final ImageUtil imageUtil;
    private final GCSUtil gcsUtil;
    @Value("${update.profile.image.duration}")
    private int minutes;

    @Transactional
    public ProfileImageDto createNewProfileImage(RequestNewProfileImageDto newProfileImageDto, Long userId) throws BadRequestException, ConflictException {
        ProfileEntity profile = profileRepository.findByUserId(userId).orElseThrow(() -> new  NotFoundException("The profile doesn't exist!"));
        if (profile.getProfileImage() != null) {
            throw new ConflictException("The profile image has already exists!");
        }
        ByteArrayInputStream thumbnail = imageUtil.resizeImage(newProfileImageDto.file(), "profileImage");
        try {
            String url = gcsUtil.uploadToGCS(newProfileImageDto.file(), thumbnail);
            ProfileImageEntity image = new ProfileImageEntity(url);
            profileImageRepository.save(image);
            profile.setProfileImage(image);
            profileRepository.save(profile);
            return profileImageMapper.profileImageEntityToProfileImageDto(image);
        } catch (IOException e) {
            throw new InternalServerErrorException("Something went wrong!");
        }
    }

    @Transactional
    public void updateNewProfileImage(RequestNewProfileImageDto newProfileImageDto, Long userId) throws NotFoundException, BadRequestException, ConflictException {
        ProfileEntity profile = profileRepository.findByUserId(userId).orElseThrow(() -> new  NotFoundException("The profile doesn't exist!"));
        if (profile.getProfileImage() == null) {
            throw new NotFoundException("The profile image doesn't exist!");
        }
        allowUpdateProfileImage(profile.getProfileImage());
        ProfileImageEntity oldImage = profile.getProfileImage();
        ByteArrayInputStream thumbnail = imageUtil.resizeImage(newProfileImageDto.file(), "profileImage");
        try {
            String url = gcsUtil.uploadToGCS(newProfileImageDto.file(), thumbnail);
            ProfileImageEntity image = new ProfileImageEntity(url);
            profileImageRepository.save(image);
            profile.setProfileImage(image);
            profileRepository.save(profile);
        } catch (IOException e) {
            throw new InternalServerErrorException("Something went wrong!");
        }
    }

    private void allowUpdateProfileImage(ProfileImageEntity image) {
        if (!Instant.now().isAfter(image.getModifiedAt().plus(Duration.ofMinutes(minutes)))) {
            throw new ConflictException(String.format("The profile image can be updated every %d minutes", minutes));
        }
    }

}
