package com.findme.post.mapper;

import com.findme.post.dto.response.MapPostDto;
import com.findme.post.dto.response.MyPostsDto;
import com.findme.post.dto.response.PostDto;
import com.findme.post.dto.response.PostProfileDto;
import com.findme.post.model.PostEntity;
import com.findme.postcomment.dto.response.PostCommentDto;
import com.findme.postcomment.mapper.PostCommentMapper;
import com.findme.postimage.dto.response.PostImageDto;
import com.findme.postimage.mapper.PostImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostImageMapper postImageMapper;
    private final PostCommentMapper postCommentMapper;

    public PostDto postEntityToPostDto(PostEntity post) {
        List<PostImageDto> images = post.getPostImage().stream()
                .map(postImageMapper::postImageEntityToPostImageDto)
                .toList();
        List<PostCommentDto> comments = post.getPostComment().stream()
                .map(postCommentMapper::postCommentEntityToPostCommentDto)
                .toList();
        PostProfileDto profile = new PostProfileDto(post.getProfile().getId(), post.getProfile().getUser().getUsername());
        return new PostDto(post.getId(), post.getDescription(), post.getViews(), post.getLongitude(), post.getLatitude(), images, comments, profile);
    }

    public List<MapPostDto> postEntityToMapPosts(List<PostEntity> postEntities) {
        List<MapPostDto> posts = new ArrayList<>();
        for (PostEntity postEntity: postEntities) {
            MapPostDto mapPostDto = new MapPostDto(postEntity.getId(), postEntity.getLongitude(), postEntity.getLatitude());
            posts.add(mapPostDto);
        }
        return posts;
    }

    public List<MyPostsDto> postEntityToMyPosts(List<PostEntity> postEntities) {
        List<MyPostsDto> posts = new ArrayList<>();
        for (PostEntity postEntity: postEntities) {
            List<PostImageDto> images = postEntity.getPostImage().stream()
                    .map(postImageMapper::postImageEntityToPostImageDto)
                    .toList();
            posts.add(new MyPostsDto(postEntity.getId(), images));
        }
        return posts;
    }

}
