package org.vnsemkin.semkinmiddleservice.domain.services;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(@NonNull String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean checkPassword(@NonNull String rawPassword, @NonNull String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}