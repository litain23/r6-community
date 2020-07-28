package me.r6_search.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AWSS3Service {
    private AmazonS3 amazonS3;
    private String bucketName;

    public List<String> uploadFile(MultipartFile[] files) {
        if(files == null) return Collections.EMPTY_LIST;
        List<String> fileNameList = new ArrayList<>();
        try {
            for(MultipartFile file : files) {
                File saveFile = multipartFileToFile(file);
                fileNameList.add(uploadFileToS3Bucket(bucketName, saveFile));
                saveFile.delete();
            }
        } catch (IOException e) {

        } catch (AmazonServiceException e) {

        }
        return fileNameList;
    }

    private File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw e;
        }
        return file;
    }

    private String uploadFileToS3Bucket(String bucketName, File file) {
        String uniqueFileName = LocalDateTime.now() + "_" + file.getName();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);
        amazonS3.putObject(putObjectRequest);
        return uniqueFileName;
    }
}