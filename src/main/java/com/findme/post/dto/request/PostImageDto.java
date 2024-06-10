package com.findme.post.dto.request;

import com.findme.profileimage.validator.IsImage;
import com.findme.profileimage.validator.IsOversize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostImageDto {
    @IsOversize
    @IsImage
    private MultipartFile file;
}
