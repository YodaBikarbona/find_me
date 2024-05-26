package com.findme.user.controller;

import com.findme.user.dao.request.LoginDao;
import com.findme.user.dao.request.NewUserDao;
import com.findme.user.dao.response.CreatedUserDao;
import com.findme.user.dao.response.ResponseLoginDao;
import com.findme.user.service.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreatedUserDao register(@RequestBody NewUserDao user) throws BadRequestException {
        return userService.registerUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseLoginDao login(@RequestBody LoginDao user) throws BadRequestException {
        return userService.loginUser(user);
    }
}
