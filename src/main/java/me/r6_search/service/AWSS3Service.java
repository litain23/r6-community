package me.r6_search.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import me.r6_search.model.imgsrc.ImgSrcRepository;
import me.r6_search.utils.HashFunction;
import me.r6_search.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AWSS3Service {
    private final ImgSrcService imgSrcService;
    private final AmazonS3 amazonS3;
    private final static String CDN_URL = "https://d13wxwpxw6qcg.cloudfront.net/";

    @Value("#{systemEnvironment['aws_s3_bucket_name']}")
    private String bucketName;


    public List<String> uploadFile(MultipartFile[] files) {
        if(files == null) return Collections.EMPTY_LIST;

        List<String> fileNameList = new ArrayList<>();
        List<String> originFileNameList = new ArrayList<>();
        try {
            for(MultipartFile file : files) {
                File saveFile = multipartFileToFile(file);
                fileNameList.add(CDN_URL + uploadFileToS3Bucket(bucketName, saveFile));
                originFileNameList.add(saveFile.getName());
                saveFile.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException("File save fail");
        } catch (AmazonServiceException e) {
            throw new RuntimeException("File upload fail");
        } catch (SdkClientException e) {
            throw new RuntimeException("S3 Connect fail");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Img Hash Fail");
        }

        imgSrcService.saveImgSrc(fileNameList, originFileNameList);
        return fileNameList;
    }

    private File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return file;
    }

    private String uploadFileToS3Bucket(String bucketName, File file) throws SdkClientException, NoSuchAlgorithmException {
        HashFunction hashFunction = new HashFunction();
        String hashedFileName = hashFunction.generateHashString(LocalDateTime.now() + "_" + file.getName());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, hashedFileName, file);
        amazonS3.putObject(putObjectRequest);
        return hashedFileName;
    }
}
