package com.volunteer.service;

import com.volunteer.dto.auth.JwtResponse;
import com.volunteer.dto.auth.LoginRequest;
import com.volunteer.dto.auth.SignupRequest;
import com.volunteer.entity.Account;
import com.volunteer.entity.Volunteer;
import com.volunteer.enums.Role;
import com.volunteer.repository.AccountRepository;
import com.volunteer.repository.VolunteerRepository;
import com.volunteer.security.jwt.JwtUtils;
import com.volunteer.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public JwtResponse signup(SignupRequest signUpRequest) {
        logger.info("Signup method called for email: {}", signUpRequest.getEmail());
        if (accountRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create Account
        Account account = new Account();
        account.setEmail(signUpRequest.getEmail());
        account.setPassword(encoder.encode(signUpRequest.getPassword()));
        account.setRole(signUpRequest.getRole());
        account.setActive(true);
        accountRepository.save(account);
        System.out.println("Account created");
        // Create Volunteer
        Volunteer volunteer = new Volunteer();
        volunteer.setFullName(signUpRequest.getFullName());
        volunteer.setAccount(account);
        volunteerRepository.save(volunteer);
        // Auto-login after signup
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(signUpRequest.getEmail());
        loginRequest.setPassword(signUpRequest.getPassword());
        return signin(loginRequest);
    }

    public JwtResponse signin(LoginRequest loginRequest) {
        logger.info("Signin method called for email: {}", loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        Optional<Volunteer> volunteer = volunteerRepository.findByAccount_Email(loginRequest.getEmail());
        return new JwtResponse(jwt, refreshToken, volunteer.get().getId(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), userDetails.getAuthorities().iterator().next().getAuthority());
    }
}