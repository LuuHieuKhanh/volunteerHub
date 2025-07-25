package com.volunteer.service;

import com.volunteer.service.impl.FileStorageInterfaceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("localStorageService")
public class LocalStorageService implements FileStorageInterfaceService {

    private final String UPLOAD_DIR = "uploads/";
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, file.getBytes());
        return "/" + UPLOAD_DIR + fileName;
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        Path path = Paths.get(fileUrl);
        Files.deleteIfExists(path);
    }
}
