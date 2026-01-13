package com.volunteer.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.volunteer.service.impl.FileStorageInterfaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;

import java.io.IOException;
import java.util.UUID;

@Service("awsS3StorageService")
public class AwsS3StorageService implements FileStorageInterfaceService {

    private final String UPLOAD_DIR = "uploads/";
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${app.file-base-url}")
    private String domain;

    private final AmazonS3 amazonS3;

    public AwsS3StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(bucketName, key);
    }

    @Override
    public String getFullFileUrl(String fileName) {
        return domain + fileName;
    }
}
