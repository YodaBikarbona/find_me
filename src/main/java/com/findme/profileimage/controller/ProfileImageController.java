package com.findme.profileimage.controller;

import com.findme.authorization.Authorization;
import com.findme.profileimage.dto.request.RequestNewProfileImageDto;
import com.findme.profileimage.dto.response.ProfileImageDto;
import com.findme.profileimage.service.ProfileImageService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/profile/image")
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/new", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public ProfileImageDto createNewProfile(@ModelAttribute RequestNewProfileImageDto image, HttpServletRequest request) throws BadRequestException {
        return profileImageService.createNewProfileImage(image, Long.parseLong(request.getAttribute("userId").toString()));
    }

}
