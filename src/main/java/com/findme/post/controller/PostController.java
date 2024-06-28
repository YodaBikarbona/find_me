package com.findme.post.controller;

import com.findme.authorization.Authorization;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.post.dto.request.GetPostDto;
import com.findme.post.dto.request.RequestMyPostsDto;
import com.findme.post.dto.request.RequestPostDto;
import com.findme.post.dto.request.RequestPostsDto;
import com.findme.post.dto.response.MapPostDto;
import com.findme.post.dto.response.PostsDto;
import com.findme.post.dto.response.PostDto;
import com.findme.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public void createNewPost(@Valid @ModelAttribute RequestPostDto postRequestDto, HttpServletRequest request) throws InternalServerErrorException {
        postService.createNewPost(postRequestDto, Long.parseLong(request.getAttribute("userId").toString()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public List<MapPostDto> getMapPosts(@Min(-180) @Max(180) @NotNull @RequestParam float longitude,
                                        @Min(-90) @Max(90) @NotNull @RequestParam float latitude,
                                        @Nullable @Min(1) @RequestParam Integer radius,
                                        @NotNull @Min(5) @Max(100) @RequestParam int limit,
                                        HttpServletRequest request) throws InternalServerErrorException {
        return postService.getMapPosts(new RequestPostsDto(longitude, latitude, radius, limit), Long.parseLong(request.getAttribute("userId").toString()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public PostDto getPost(@PathVariable long id,
                           @Min(-180) @Max(180) @NotNull @RequestParam float longitude,
                           @Min(-90) @Max(90) @NotNull @RequestParam float latitude,
                           HttpServletRequest request) throws NotFoundException {
        return postService.getPost(new GetPostDto(id, latitude, longitude), Long.parseLong(request.getAttribute("userId").toString()));
    } 

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/myPosts", produces = MediaType.APPLICATION_JSON_VALUE)
    @Authorization
    public List<PostsDto> getMyPost(@Nullable @Min(0) @RequestParam Integer offset,
                                    @NotNull @Min(5) @Max(100) @RequestParam int limit,
                                    @NotNull @RequestParam boolean myPosts,
                                    HttpServletRequest request) throws NotFoundException {
        return postService.getPosts(new RequestMyPostsDto(offset, limit, myPosts), Long.parseLong(request.getAttribute("userId").toString()));
    }

}
