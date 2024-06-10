package com.findme.post.controller;

import com.findme.authorization.Authorization;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.post.dto.request.PostRequestDto;
import com.findme.post.dto.response.PostDto;
import com.findme.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public PostDto createNewProfile(@ModelAttribute PostRequestDto postRequestDto, HttpServletRequest request) throws InternalServerErrorException {
        return postService.createNewPost(postRequestDto, Long.parseLong(request.getAttribute("userId").toString()));
    }

}
