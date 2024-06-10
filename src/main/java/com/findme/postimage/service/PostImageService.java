package com.findme.postimage.service;

import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.post.model.PostEntity;
import com.findme.postimage.model.PostImageEntity;
import com.findme.postimage.repository.PostImageRepository;
import com.findme.utils.GCSUtil;
import com.findme.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final ImageUtil imageUtil;
    private final GCSUtil gcsUtil;
    @Value("${update.profile.image.duration}")
    private int minutes;

    @Transactional
    public PostImageEntity createNewPostImage(MultipartFile file, PostEntity post) throws BadRequestException, ConflictException {
        ByteArrayInputStream thumbnail = imageUtil.resizeImage(file, "post");
        try {
            String url = gcsUtil.uploadToGCS(file, thumbnail);
            PostImageEntity image = new PostImageEntity(url, post);
            postImageRepository.save(image);
            return image;
        } catch (IOException e) {
            throw new InternalServerErrorException("Something went wrong!");
        }
    }

}
