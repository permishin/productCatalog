package com.example.productcatalog.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class S3Amazon {

    @Value("${s3.amazon.bucket}")
    private String bucket;

    //Соединение с сервером Амазон
    public AmazonS3 initConnect() {
        AWSCredentials credentials = new BasicAWSCredentials(
                System.getenv("ACCESS_KEY"),
                System.getenv("SECRET_KEY")
        );
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
        return s3client;
    }
    //Загрузка фото на сервер
    public void uploadFile(String resultFileName, MultipartFile file) {
        File fileObj = convert(file);
        initConnect().putObject(new PutObjectRequest(bucket, resultFileName, fileObj)
        );
    }

    public File convert(MultipartFile file)
    {
        File convFile = new File(file.getOriginalFilename());
        try ( FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e){
        }
        return convFile;
    }
    //Удаление фото на сервер
    public void deleteFile(String fileName) {
        initConnect().deleteObject(bucket, fileName);
    }
}
