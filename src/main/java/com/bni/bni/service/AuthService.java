package com.bni.bni.service;

import com.bni.bni.entity.User;
import com.bni.bni.repository.UserRepository;
import com.bni.bni.util.JwtUtil;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final UserRepository repo;

    AuthService(PasswordEncoder encoder, JwtUtil jwtUtil, UserRepository repo) {
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.repo = repo;
    }


    public String register(String username, String password) {
        if (repo.existsByUsername(username)) {
            logger.warn("PERCOBAAN REGSTRASI DENGAN MENGGUNAKAN USER TERDAFTAR: {}", username);
            return "User already exists";
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encoder.encode(password));
        user.setRole("USER");
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
        user.setActive(true);
        repo.save(user);

        logger.warn("PERCOBAAN REGISTER BERHASIL: {}", username);
        return "Registered successfully";
    }

    public String login(String username, String password) {
        Optional<User> user = repo.findByUsername(username);
        if (user.isPresent() && encoder.matches(password, user.get().getPasswordHash())) {
            logger.warn("PERCOBAAN LOGIN BERHASIL: {}", username);
            return jwtUtil.generateToken(username, user.get().getRole());
        }

        logger.warn("PERCOBAAN LOGIN GAGAL, USERNAME ATAU PASSWORD SALAH: {}", username);
        return null;
    }
}
