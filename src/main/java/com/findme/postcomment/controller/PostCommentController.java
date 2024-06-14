package com.findme.postcomment.controller;

import com.findme.authorization.Authorization;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.postcomment.dto.request.NewPostCommentDto;
import com.findme.postcomment.dto.response.PostCommentDto;
import com.findme.postcomment.service.PostCommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public PostCommentDto createNewPostComment(@Valid @RequestBody NewPostCommentDto newPostCommentDto, @PathVariable long postId, HttpServletRequest request) throws InternalServerErrorException {
        return postCommentService.createNewPostComment(newPostCommentDto, Long.parseLong(request.getAttribute("userId").toString()), postId);
    }

}
