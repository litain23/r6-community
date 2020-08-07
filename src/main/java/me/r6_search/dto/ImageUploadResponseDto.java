package me.r6_search.dto;

import lombok.Data;

import java.util.List;

@Data
public class ImageUploadResponseDto {
    List<String> imageUrl;

    public ImageUploadResponseDto(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }
}
