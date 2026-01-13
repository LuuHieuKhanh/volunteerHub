package com.volunteer.Utility;

import org.springframework.beans.factory.annotation.Value;

public class FileUtil {
    public static String buildFileUrl(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }
        return AppConfig.FILE_BASE_URL + fileName;
    }
}
