package com.findme.profileimage.service;

import com.findme.exceptions.ConflictException;
import com.findme.exceptions.InternalServerErrorException;
import com.findme.profile.model.ProfileEntity;
import com.findme.profile.repository.ProfileRepository;
import com.findme.profileimage.dto.request.RequestNewProfileImageDto;
import com.findme.profileimage.dto.response.ProfileImageDto;
import com.findme.profileimage.mapper.ProfileImageMapper;
import com.findme.profileimage.model.ProfileImageEntity;
import com.findme.profileimage.repository.ProfileImageRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageMapper profileImageMapper;

    public ProfileImageService(ProfileImageRepository profileImageRepository, ProfileRepository profileRepository, ProfileImageMapper profileImageMapper) {
        this.profileImageRepository = profileImageRepository;
        this.profileRepository = profileRepository;
        this.profileImageMapper = profileImageMapper;
    }

    @Transactional
    public ProfileImageDto createNewProfileImage(RequestNewProfileImageDto newProfileImageDto, Long userId) throws BadRequestException, ConflictException {
        ProfileEntity profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            throw new BadRequestException("The profile doesn't exist!");
        }
        if (profile.getProfileImage() != null) {
            throw new ConflictException("The profile image has already exists!");
        }
        ByteArrayInputStream thumbnail = resizeImage(newProfileImageDto.getFile());
        try {
            String url = uploadToGCS(newProfileImageDto.getFile(), thumbnail);
            ProfileImageEntity image = new ProfileImageEntity(url);
            profileImageRepository.save(image);
            profile.setProfileImage(image);
            profileRepository.save(profile);
            return profileImageMapper.profileImageEntityToProfileImageDto(image);
        } catch (IOException e) {
            throw new InternalServerErrorException("Something went wrong!");
        }
    }

    private ByteArrayInputStream resizeImage(MultipartFile file) throws BadRequestException {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BadRequestException("The image valid!");
            }
            int width = image.getWidth();
            int height = image.getHeight();
            String extension = getImageExtension(file);
            checkImageResolution(image);
            ByteArrayOutputStream outputStream = cutTheImage(image, width, height, extension);
            byte[] thumbnail = outputStream.toByteArray();
            return new ByteArrayInputStream(thumbnail);
        } catch (IOException e) {
            throw new BadRequestException("Something went wrong uploading the image!");
        }
    }

    private void checkImageResolution(BufferedImage image) throws BadRequestException {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width >= 2000 || height >= 2000) {
            throw new BadRequestException("Max resolution is 1000x1000 pixels!");
        }
    }

    private ByteArrayOutputStream cutTheImage(BufferedImage image, int width, int height, String extension) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int maxResolution = 450;
        if (width > maxResolution || height > maxResolution) {
            Thumbnails.of(image)
                    .size(maxResolution, maxResolution)
                    .outputFormat(extension)
                    .toOutputStream(outputStream);
        } else {
            ImageIO.write(image, extension, outputStream);
        }
        return outputStream;
    }

    private String getImageExtension(MultipartFile file) throws BadRequestException {
        String originalFilename = file.getOriginalFilename();
        String extension = null;
        List<String> validExtensions = Arrays.asList("jpg", "png", "jpeg");
        if (originalFilename == null) {
            throw new BadRequestException("The image is invalid!");
        }
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1);
        } else {
            extension = originalFilename.substring(dotIndex);
        }
        if (!validExtensions.contains(extension)) {
            throw new BadRequestException("The extension of the image is invalid!");
        }
        return extension;
    }

    public Storage getStorage() throws IOException {
        String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes()));
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    private String uploadToGCS(MultipartFile file, ByteArrayInputStream image) throws IOException {
        String gcsBaseUrl = System.getenv("GCS_BASE_URL");
        String bucketName = System.getenv("BUCKET_NAME");
        if (gcsBaseUrl == null || bucketName == null) {
            throw new InternalServerErrorException("Something went wrong!");
        }
        Storage storage = getStorage();
        String extension = getImageExtension(file);
        String newFilename = String.format("%s.%s", UUID.randomUUID().toString(), extension);
        String blobName = "uploads/" + newFilename;
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(image.readAllBytes());
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        Blob blob = storage.create(blobInfo, imageBytes);
        return String.format("%s%s/%s", gcsBaseUrl, bucketName, blob.getName());
    }
}
