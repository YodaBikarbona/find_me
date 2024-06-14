package com.findme.postcomment.service;

import com.findme.exceptions.InternalServerErrorException;
import com.findme.exceptions.NotFoundException;
import com.findme.post.model.PostEntity;
import com.findme.post.repository.PostRepository;
import com.findme.postcomment.dto.request.NewPostCommentDto;
import com.findme.postcomment.dto.response.PostCommentDto;
import com.findme.postcomment.mapper.PostCommentMapper;
import com.findme.postcomment.model.PostCommentEntity;
import com.findme.postcomment.repository.PostCommentRepository;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.service.ProfileService;
import com.findme.utils.ApplicationCtxHolderUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final ProfileService profileService;
    private final PostCommentMapper postCommentMapper;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);

    public PostCommentDto createNewPostComment(NewPostCommentDto newPostCommentDto, long userId, long postId) throws NotFoundException, InternalServerErrorException {
        ProfileEntity profile = profileService.getProfile(userId);
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("The post doesn't exist!"));
        try {
            PostCommentEntity postCommentEntity = new PostCommentEntity(newPostCommentDto.comment(), post, profile);
            postCommentRepository.save(postCommentEntity);
            return postCommentMapper.postCommentEntityToPostCommentDto(postCommentEntity);
        } catch (Exception e) {
            logger.error("Error while creating new post comment! ex:", e);
            throw new InternalServerErrorException("Something went wrong!");
        }

    }

}
