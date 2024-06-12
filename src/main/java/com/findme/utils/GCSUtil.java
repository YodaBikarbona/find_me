package com.findme.utils;

import com.findme.exceptions.InternalServerErrorException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GCSUtil {

    private static final Logger log = LoggerFactory.getLogger(GCSUtil.class);
    private final ImageUtil imageUtil;

    public Storage getStorage() throws IOException {
        String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()));
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public String uploadToGCS(MultipartFile file, ByteArrayInputStream image) throws IOException {
        String gcsBaseUrl = System.getenv("GCS_BASE_URL");
        String bucketName = System.getenv("BUCKET_NAME");
        if (gcsBaseUrl == null || bucketName == null) {
            throw new InternalServerErrorException("Something went wrong!");
        }
        Storage storage = getStorage();
        String extension = imageUtil.getImageExtension(file);
        String newFilename = String.format("%s.%s", UUID.randomUUID().toString(), extension);
        String blobName = "uploads/" + newFilename;
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(image.readAllBytes());
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        long startTime = System.currentTimeMillis();
        log.info(String.format("Sending image to GCS, timestamp %s", startTime));
        Blob blob = storage.create(blobInfo, imageBytes);
        long endTime = System.currentTimeMillis();
        log.info(String.format("Image uploaded to GCS, timestamp %s", startTime));
        log.info(String.format("Upload duration %s", startTime - endTime));
        return String.format("%s%s/%s", gcsBaseUrl, bucketName, blob.getName());
    }

}
