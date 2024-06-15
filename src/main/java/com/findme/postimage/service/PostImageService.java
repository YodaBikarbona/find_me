package com.findme.postimage.service;

import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.post.model.PostEntity;
import com.findme.postimage.model.PostImageEntity;
import com.findme.postimage.repository.PostImageRepository;
import com.findme.utils.ApplicationCtxHolderUtil;
import com.findme.utils.GCSUtil;
import com.findme.utils.ImageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final ImageUtil imageUtil;
    private final GCSUtil gcsUtil;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);


    @Transactional
    public PostImageEntity createNewPostImage(MultipartFile file, PostEntity post) throws BadRequestException, ConflictException {
        ByteArrayInputStream thumbnail = imageUtil.resizeImage(file, "post");
        try {
            String url = gcsUtil.uploadToGCS(file, thumbnail);
            PostImageEntity image = new PostImageEntity(url, post);
            postImageRepository.save(image);
            return image;
        } catch (Exception ex) {
            logger.error("Error creating new post image!", ex);
            throw new InternalServerErrorException("Something went wrong!");
        }
    }

}
