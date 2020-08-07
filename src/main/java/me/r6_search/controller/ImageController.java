package me.r6_search.controller;

import lombok.RequiredArgsConstructor;
import me.r6_search.dto.ImageUploadRequestDto;
import me.r6_search.dto.ImageUploadResponseDto;
import me.r6_search.exception.board.PostIllegalFileExtensionException;
import me.r6_search.service.AWSS3Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/c")
@RestController
public class ImageController {
    private final AWSS3Service awss3Service;

    @PostMapping("/image")
    public ImageUploadResponseDto imageUpload(ImageUploadRequestDto requestDto) {
        checkFilesExtension(requestDto.getImageFiles());
        List<String> imgUrlList = awss3Service.uploadFile(requestDto.getImageFiles());
        return new ImageUploadResponseDto(imgUrlList);
    }

    private void checkFilesExtension(MultipartFile[] files) {
        if(files == null) return;
        for(MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            if(extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png") || extension.equals("gif")) continue;
            else throw new PostIllegalFileExtensionException("파일 확장자는 jpeg, jpg, png, gif 만 가능합니다");
        }
    }
}
