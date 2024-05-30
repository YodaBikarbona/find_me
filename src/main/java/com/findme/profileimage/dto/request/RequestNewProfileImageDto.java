package com.findme.profileimage.dto.request;

import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

public class RequestNewProfileImageDto {

    private final MultipartFile file;

    public RequestNewProfileImageDto(MultipartFile file) throws BadRequestException {
        isOversize(file);
        isImage(file);
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    private void isOversize(MultipartFile file) throws BadRequestException {
        final int maxSize = (2 * (1024 * 1024));
        if (file.getSize() > maxSize) {
            throw new BadRequestException("The image is to big!");
        }
    }

    private void isImage(MultipartFile file) throws BadRequestException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw  new BadRequestException("The file is not an image!");
        }
    }

}
