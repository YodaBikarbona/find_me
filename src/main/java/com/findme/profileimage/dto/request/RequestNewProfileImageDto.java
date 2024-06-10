package com.findme.profileimage.dto.request;

import com.findme.profileimage.validator.IsImage;
import com.findme.profileimage.validator.IsOversize;
import org.springframework.web.multipart.MultipartFile;

public record RequestNewProfileImageDto(@IsOversize @IsImage MultipartFile file) {
}
