package com.volunteer.service.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageInterfaceService {
    String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String fileUrl) throws IOException;
    String getFullFileUrl(String fileName);
}
