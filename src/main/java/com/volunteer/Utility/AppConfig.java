package com.volunteer.Utility;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig implements InitializingBean {
    @Value("${app.file-base-url}")
    private String fileBaseUrl;

    public static String FILE_BASE_URL;

    @Override
    public void afterPropertiesSet() {
        FILE_BASE_URL = fileBaseUrl;
    }
}