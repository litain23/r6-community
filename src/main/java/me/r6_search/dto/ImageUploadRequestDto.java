package me.r6_search.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadRequestDto {
    MultipartFile[] imageFiles;
}
