package com.findme.utils;

import jakarta.validation.constraints.NotNull;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ImageUtil {

    public String getImageExtension(MultipartFile file) throws BadRequestException {
        String originalFilename = file.getOriginalFilename();
        String extension = null;
        List<String> validExtensions = Arrays.asList("jpg", "jpeg");
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

    public ByteArrayInputStream resizeImage(MultipartFile file, @NotNull String imagePurpose) throws BadRequestException {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BadRequestException("The image valid!");
            }
            int width = image.getWidth();
            int height = image.getHeight();
            String extension = getImageExtension(file);
            checkImageResolution(image, imagePurpose);
            ByteArrayOutputStream outputStream = cutTheImage(image, width, height, extension, imagePurpose);
            byte[] thumbnail = outputStream.toByteArray();
            return new ByteArrayInputStream(thumbnail);
        } catch (IOException e) {
            throw new BadRequestException("Something went wrong uploading the image!");
        }
    }

    private void checkImageResolution(BufferedImage image, String imagePurpose) throws BadRequestException {
        int maxResolution = getUploadImageKindMaxResolution(imagePurpose);
        int width = image.getWidth();
        int height = image.getHeight();
        if (width >= maxResolution || height >= maxResolution) {
            throw new BadRequestException(String.format("Maximum image resolution is %d x %d", maxResolution, maxResolution));
        }
    }

    private int getUploadImageKindMaxResolution(String imagePurpose) {
        if (imagePurpose.equals("post")) {
            return 10000;
        }
        return 2000;
    }

    private int getImageKindMaxResolution(String imagePurpose) {
        if (imagePurpose.equals("post")) {
            return 5000;
        }
        return 450;
    }

    private ByteArrayOutputStream cutTheImage(BufferedImage image, int width, int height, String extension, @NotNull String imagePurpose) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final int maxResolution = getImageKindMaxResolution(imagePurpose);
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

}
