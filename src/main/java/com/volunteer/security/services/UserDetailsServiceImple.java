package com.volunteer.security.services;

import com.volunteer.entity.Account;
import com.volunteer.entity.Volunteer;
import com.volunteer.repository.AccountRepository;
import com.volunteer.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImple implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VolunteerRepository volunteerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> accountOpt = accountRepository.findByEmailAndIsActiveTrue(email);
        if (!accountOpt.isPresent()) {
            throw new UsernameNotFoundException("Account not found or inactive with email: " + email);
        }
        Account account = accountOpt.get();
        Optional<Volunteer> volunteerOpt = volunteerRepository.findByEmailAndIsActiveTrue(email);
        Volunteer volunteer = volunteerOpt.orElse(null);
        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole().toString());
        return new UserDetailsImpl(
                account.getId(),
                volunteer != null ? volunteer.getFullName() : account.getEmail(),
                account.getEmail(),
                account.getPassword(),
                Collections.singletonList(authority),
                account.isActive()
        );
    }
} 