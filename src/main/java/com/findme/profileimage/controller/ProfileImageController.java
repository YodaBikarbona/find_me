package com.findme.profileimage.controller;

import com.findme.authorization.Authorization;
import com.findme.profileimage.dto.request.RequestNewProfileImageDto;
import com.findme.profileimage.dto.response.ProfileImageDto;
import com.findme.profileimage.service.ProfileImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profile/image")
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public ProfileImageDto createNewProfileImage(@Valid @ModelAttribute RequestNewProfileImageDto image, HttpServletRequest request) throws BadRequestException {
        return profileImageService.createNewProfileImage(image, Long.parseLong(request.getAttribute("userId").toString()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public void updateProfileImage(@Valid @ModelAttribute RequestNewProfileImageDto image, HttpServletRequest request) throws BadRequestException {
        profileImageService.updateNewProfileImage(image, Long.parseLong(request.getAttribute("userId").toString()));
    }

}
