package com.volunteer.service;

import com.volunteer.service.impl.FileStorageInterfaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

@Service("localStorageService")
public class LocalStorageService implements FileStorageInterfaceService {

    private final String UPLOAD_DIR = "uploads/";
    @Value("${app.file-base-url}")
    private String domain;


    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Lấy tên gốc
        String originalFileName = file.getOriginalFilename();

        // Loại bỏ khoảng trắng + dấu tiếng Việt
        String cleanedFileName = removeAccents(originalFileName)
                .replaceAll("\\s+", "_")        // thay khoảng trắng bằng _
                .replaceAll("[^a-zA-Z0-9._-]", ""); // giữ lại ký tự hợp lệ

        // Đảm bảo unique bằng UUID
        String uniqueFileName = UUID.randomUUID() + "_" + cleanedFileName;

        Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.write(path, file.getBytes());
        return "/" + UPLOAD_DIR + uniqueFileName;
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        Path path = Paths.get(fileUrl);
        Files.deleteIfExists(path);
    }

    @Override
    public String getFullFileUrl(String fileName) {
        if(fileName == null) {
            return null;
        }
        return domain + fileName;
    }

    private String removeAccents(String text) {
        if (text == null) return null;
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
