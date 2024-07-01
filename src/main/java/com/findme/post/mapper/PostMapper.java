package com.findme.post.mapper;

import com.findme.post.dto.response.MapPostDto;
import com.findme.post.dto.response.PostsDto;
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
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostImageMapper postImageMapper;
    private final PostCommentMapper postCommentMapper;

    public PostDto postEntityToPostDto(PostEntity post, long profileId, Double distance) {
        List<PostImageDto> images = post.getPostImage().stream()
                .map(postImageMapper::postImageEntityToPostImageDto)
                .toList();
        List<PostCommentDto> comments = post.getPostComment().stream()
                .map(postCommentMapper::postCommentEntityToPostCommentDto)
                .toList();
        PostProfileDto profile = new PostProfileDto(post.getProfile().getId(), post.getProfile().getUser().getUsername());
        boolean following = post.getProfile().getFollowers().isEmpty() ? Boolean.FALSE : post.getProfile().getFollowers().stream()
                .anyMatch(follower -> Objects.equals(follower.getFollower().getId(), profileId));
        return new PostDto(
                post.getId(),
                post.getDescription(),
                post.getViews(),
                post.getLongitude(),
                post.getLatitude(),
                images,
                comments,
                profile,
                following,
                distance);
    }

    public List<MapPostDto> postEntityToMapPosts(List<Object[]> postsData) {
        List<MapPostDto> posts = new ArrayList<>();
        for (Object[] postData: postsData) {
            MapPostDto mapPostDto = new MapPostDto((long) postData[0], (double) postData[1], (double) postData[2], (double) postData[3]);
            posts.add(mapPostDto);
        }
        return posts;
    }

    public List<PostsDto> postEntityToMyPosts(List<PostEntity> postEntities) {
        List<PostsDto> posts = new ArrayList<>();
        for (PostEntity postEntity: postEntities) {
            List<PostImageDto> images = postEntity.getPostImage().stream()
                    .map(postImageMapper::postImageEntityToPostImageDto)
                    .toList();
            posts.add(new PostsDto(postEntity.getId(), images));
        }
        return posts;
    }

}
