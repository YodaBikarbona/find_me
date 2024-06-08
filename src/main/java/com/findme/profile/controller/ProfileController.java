package com.findme.profile.controller;

import com.findme.authorization.Authorization;
import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.profile.dto.request.EditProfileDto;
import com.findme.profile.dto.request.NewProfileDto;
import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/myProfile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public ProfileDto getProfile(HttpServletRequest request) throws NotFoundException {
        return profileService.fetchProfile(Long.parseLong(request.getAttribute("userId").toString()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public ProfileDto createNewProfile(@Valid @RequestBody NewProfileDto newProfileDto, HttpServletRequest request) throws ConflictException, InternalServerErrorException {
        return profileService.createNewProfile(newProfileDto, Long.parseLong(request.getAttribute("userId").toString()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/myProfile/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public void editProfile(@Valid @RequestBody EditProfileDto editProfileDto, HttpServletRequest request) throws InternalServerErrorException {
        profileService.editProfile(editProfileDto, Long.parseLong(request.getAttribute("userId").toString()));
    }

}
