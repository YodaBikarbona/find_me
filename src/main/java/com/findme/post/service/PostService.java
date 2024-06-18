package com.findme.post.service;

import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NoPermissionException;
import com.findme.exceptions.NotFoundException;
import com.findme.post.dto.request.RequestMyPostsDto;
import com.findme.post.dto.request.RequestPostDto;
import com.findme.post.dto.request.RequestPostsDto;
import com.findme.post.dto.response.MapPostDto;
import com.findme.post.dto.response.MyPostsDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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
    public void createNewPost(RequestPostDto data, long userId) throws InternalServerErrorException, NoPermissionException {
        ProfileEntity profile = profileService.getProfile(userId);
        if (!profile.getUser().getActivated()) {
            throw new NoPermissionException("The profile is not active!");
        }
        Instant startDay = Instant.now().atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endDay = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();
        if (postRepository.countByCreatedAtBetween(startDay, endDay) > 4) {
            throw new NoPermissionException("The limit of 5 posts are reached!");
        }
        try {
            PostEntity post = new PostEntity(data.getNewPostDto().getDescription(), data.getNewPostDto().getLongitude(), data.getNewPostDto().getLatitude(), profile);
            postRepository.save(post);
            logger.info("The new post has successfully created!");
            PostImageEntity image = postImageService.createNewPostImage(data.getPostImageDto().getFile(), post);
            post.addPostImage(image);
            logger.info("The post image has successfully created!");
        } catch (Exception ex) {
            logger.error("The post cannot be successfully created!", ex);
            throw new InternalServerErrorException("Internal Server Error!");
        }
    }

    public List<MapPostDto> getMapPosts(RequestPostsDto postsDto, long userId) throws InternalServerErrorException {
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
        try {
            post.increaseViews();
            postRepository.save(post);
            return postMapper.postEntityToPostDto(post);
        } catch (Exception ex) {
            logger.error("The post cannot be successfully fetched or the views cannot be updated!", ex);
            throw new InternalServerErrorException("Internal Server Error!");
        }
    }

    public List<MyPostsDto> getMyPosts(RequestMyPostsDto myPostsDto, long userId) throws NotFoundException {
        ProfileEntity profile = profileService.getProfile(userId);
        Pageable pageable = PageRequest.of(!Objects.isNull(myPostsDto.offset()) ? myPostsDto.offset() : 0, myPostsDto.limit(), Sort.by("createdAt").descending());
        List<PostEntity> posts = postRepository.findMyPosts(profile.getId(), pageable);
        if (posts.isEmpty()) {
            throw new NotFoundException("The posts don't exist!");
        }
        return postMapper.postEntityToMyPosts(posts);
    }

}
