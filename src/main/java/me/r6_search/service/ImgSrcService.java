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
    protected void saveImgSrc(List<String> imgFileNameList, List<String> originImgFileNameList) {
        for(int i=0;i<imgFileNameList.size();i++) {
            String originFileName = originImgFileNameList.get(i);
            String fileName = imgFileNameList.get(i);

            ImgSrc imgSrc = ImgSrc.builder()
                    .src(fileName)
                    .name(originFileName)
                    .build();
            imgSrcRepository.save(imgSrc);
        }
    }

    @Transactional
    protected void updateImgSrc(List<String> imgFileNameList, Post post) {
        for(String fileName : imgFileNameList) {
            ImgSrc imgSrc = imgSrcRepository.findBySrc(fileName);
            if(imgSrc == null) continue;
            if(imgSrc.getPost() != null) {
                // hash 함수가 동일하게 나오면 새로운 값을 만들어서 넣어줌 .. 이럴일을 없겠지..?
                ImgSrc newImgSrc = ImgSrc.builder()
                        .src(fileName)
                        .name("new-image-name")
                        .post(post)
                        .build();

                imgSrcRepository.save(newImgSrc);
            } else {
                imgSrc.updatePost(post);
            }

        }
    }

}
