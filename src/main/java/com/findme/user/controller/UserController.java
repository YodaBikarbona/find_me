package com.findme.user.controller;

import com.findme.authorization.RefreshAuthorization;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.user.dto.request.RequestLoginDto;
import com.findme.user.dto.request.RequestNewUserDto;
import com.findme.user.dto.response.NewUserDto;
import com.findme.user.dto.response.CredentialsDto;
import com.findme.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public NewUserDto register(@Valid @RequestBody RequestNewUserDto user) throws InternalServerErrorException {
        return userService.registerUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public CredentialsDto login(@RequestBody RequestLoginDto user) throws InternalServerErrorException {
        return userService.loginUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    @RefreshAuthorization
    public CredentialsDto refreshTokens(HttpServletRequest request) throws InternalServerErrorException {
        return userService.refreshTokens(Long.parseLong(request.getAttribute("userId").toString()));
    }
}
