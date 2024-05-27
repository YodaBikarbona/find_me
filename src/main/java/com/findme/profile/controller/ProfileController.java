package com.findme.profile.controller;

import com.findme.authorization.Authorization;
import com.findme.exceptions.NotFoundException;
import com.findme.profile.dto.request.RequestNewProfileDto;
import com.findme.profile.dto.response.ProfileDto;
import com.findme.profile.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public ProfileDto createNewProfile(@RequestBody RequestNewProfileDto profile, HttpServletRequest request) throws BadRequestException {
        return profileService.createNewProfile(profile, Long.parseLong(request.getAttribute("userId").toString()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public ProfileDto getProfile(@PathVariable("id") Long id, HttpServletRequest request) throws NotFoundException {
        return profileService.fetchProfile(Long.parseLong(request.getAttribute("userId").toString()));
    }

}
