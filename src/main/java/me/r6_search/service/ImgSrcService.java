package me.r6_search.service;

import lombok.RequiredArgsConstructor;
import me.r6_search.model.imgsrc.ImgSrc;
import me.r6_search.model.imgsrc.ImgSrcRepository;
import me.r6_search.model.post.Post;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImgSrcService {
    private final ImgSrcRepository imgSrcRepository;

    @Transactional
    protected void saveImgSrc(List<String> imgFileNameList) {
        for(String fileName : imgFileNameList) {
            ImgSrc imgSrc = ImgSrc.builder()
                    .src(fileName)
                    .build();

            imgSrcRepository.save(imgSrc);
        }
    }

    @Transactional
    protected void updateImgSrc(List<String> imgFileNameList, Post post) {
        for(String fileName : imgFileNameList) {
            ImgSrc imgSrc = imgSrcRepository.findBySrc(fileName);
            if(imgSrc == null) continue;

            imgSrc.updatePost(post);
        }
    }

}
