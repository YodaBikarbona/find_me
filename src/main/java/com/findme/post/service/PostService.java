package com.findme.post.service;

import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.post.dto.request.RequestPostDto;
import com.findme.post.dto.request.RequestPostsDto;
import com.findme.post.dto.response.MapPostDto;
import com.findme.post.dto.response.PostDto;
import com.findme.post.mapper.PostMapper;
import com.findme.post.model.PostEntity;
import com.findme.post.repository.PostRepository;
import com.findme.postimage.model.PostImageEntity;
import com.findme.postimage.service.PostImageService;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.service.ProfileService;
import com.findme.utils.ApplicationCtxHolderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final PostMapper postMapper;
    private final ProfileService profileService;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);

    @Transactional
    public PostDto createNewPost(RequestPostDto data, long userId) throws InternalServerErrorException {
        ProfileEntity profile = profileService.getProfile(userId);
        try {
            PostEntity post = new PostEntity(data.getNewPostDto().getDescription(), data.getNewPostDto().getLongitude(), data.getNewPostDto().getLatitude(), profile);
            postRepository.save(post);
            logger.info("The new post has successfully created!");
            PostImageEntity image = postImageService.createNewPostImage(data.getPostImageDto().getFile(), post);
            post.addPostImage(image);
            logger.info("The post image has successfully created!");
            return postMapper.postEntityToPostDto(post);
        } catch (Exception ex) {
            logger.error("The post cannot be successfully created!", ex);
            throw new InternalServerErrorException("Internal Server Error!");
        }
    }

    public List<MapPostDto> getPosts(RequestPostsDto postsDto, long userId) throws InternalServerErrorException {
        ProfileEntity profile = profileService.getProfile(userId);
        List<PostEntity> posts;
        if (Objects.isNull(postsDto.radius())) {
            posts = postRepository.findNearestPosts(profile.getId(), postsDto.longitude(), postsDto.latitude(), postsDto.limit());
        } else {
            posts = postRepository.findNearestPostsWithinRadius(profile.getId(), postsDto.longitude(), postsDto.latitude(), postsDto.radius(), postsDto.limit());
        }
        return postMapper.postEntityToMapPosts(posts);
    }

    public PostDto getPost(long postId) throws NotFoundException {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The post doesn't exist!"));
        return postMapper.postEntityToPostDto(post);
    }

}
