package com.findme.cron;

import com.findme.profileimage.model.ProfileImageEntity;
import com.findme.profileimage.repository.ProfileImageRepository;
import com.findme.profileimage.service.ProfileImageService;
import com.findme.utils.ApplicationCtxHolderUtil;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GCSDeleteProfileImageJob implements Job {

    private final ProfileImageService profileImageService;
    private final ProfileImageRepository profileImageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<ProfileImageEntity> images = profileImageRepository.findAllNotLinkedImages();
        for (ProfileImageEntity image: images) {
            logger.info(String.format("Start removing the profile image, id: %d", image.getId()));
            boolean removedGCSImage = deleteGCSImage(image.getUrl());
            if (removedGCSImage) {
                deleteImage(image);
            }
        }
    }

    private void deleteImage(ProfileImageEntity image) {
        try {
            profileImageRepository.delete(image);
            logger.info("The image has successfully removed!");
        } catch (Exception e) {
            logger.error("The image cannot be removed!", e);
        }
    }

    private boolean deleteGCSImage(String url) {
        boolean removed = Boolean.FALSE;
        try {
            Storage storage = profileImageService.getStorage();
            String bucketName = System.getenv("BUCKET_NAME");
            String filename = Arrays.stream(url.split(String.format("/%s/", bucketName))).toList().getLast();
            BlobId blobId = BlobId.of(bucketName, filename);
            removed = storage.delete(blobId);
        } catch (IOException e) {
            logger.error("GCS image cannot be deleted, removing from the DB will not be triggered!", e);
        }
        if (removed) {
            logger.info("GCS image has successfully deleted!");
        } else {
            logger.info("GCS image has not successfully deleted!");
        }
        return removed;
    }

}
