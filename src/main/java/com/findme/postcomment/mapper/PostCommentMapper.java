package com.findme.postcomment.mapper;

import com.findme.postcomment.dto.response.PostCommentDto;
import com.findme.postcomment.dto.response.PostCommentProfileDto;
import com.findme.postcomment.model.PostCommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PostCommentMapper {

    public PostCommentDto postCommentEntityToPostCommentDto(PostCommentEntity comment) {
        PostCommentProfileDto profileDto = new PostCommentProfileDto(comment.getProfile().getId(), comment.getProfile().getUser().getUsername());
        return new PostCommentDto(comment.getId(), comment.getComment(), profileDto);
    }

}
