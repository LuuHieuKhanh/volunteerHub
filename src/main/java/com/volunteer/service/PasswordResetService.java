package com.volunteer.service;

import com.volunteer.entity.Account;
import com.volunteer.exception.ResourceNotFoundException;
import com.volunteer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String ALL_CHARS = UPPER_CASE + LOWER_CASE + NUMBERS + SPECIAL_CHARS;

    public void resetPassword(String email) {
        // Tìm account theo email
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with email: " + email));

        // Tạo mật khẩu mới
        String newPassword = generateRandomPassword();

        // Mã hóa mật khẩu mới
        String encodedPassword = passwordEncoder.encode(newPassword);

        // Cập nhật mật khẩu trong database
        account.setPassword(encodedPassword);
        accountRepository.save(account);

        // Gửi email với mật khẩu mới
        emailService.sendNewPasswordEmail(email, newPassword);
    }

    private String generateRandomPassword() {
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Đảm bảo có ít nhất 1 ký tự từ mỗi loại
        password.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        password.append(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Tạo độ dài ngẫu nhiên từ 6-40 ký tự (đã có 4 ký tự đầu)
        int remainingLength = 6 + random.nextInt(3); // 6-40 ký tự

        // Thêm các ký tự ngẫu nhiên cho phần còn lại
        for (int i = 4; i < remainingLength; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Trộn ngẫu nhiên các ký tự
        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        Random random = new SecureRandom();

        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }

        return new String(characters);
    }
}