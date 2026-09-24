package com.example.securetaskmanagerfinal.service;

import com.example.securetaskmanagerfinal.entity.RefreshToken;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public String createRefreshToken(User user) {
        byte[] bytes = new byte[64];

        secureRandom.nextBytes(bytes);
        String rawToken =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.DAYS)
        );
        refreshTokenRepository.save(refreshToken);
        return rawToken;

    }

    public RefreshToken validate(String rawToken) {

        String hash = hash(rawToken);

        RefreshToken token =
                refreshTokenRepository
                        .findByTokenHash(hash)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid refresh token"
                                )
                        );

        if (token.isRevoked()) {
            throw new RuntimeException(
                    "Refresh token revoked"
            );
        }

        if (token.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        return token;
    }

    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
    private String hash(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            token.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(
                        String.format("%02x", b)
                );
            }

            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void revokeAll(User user) {

        List<RefreshToken> tokens =
                refreshTokenRepository.findAllByUserIdAndRevokedFalse(
                        user.getId()
                );

        tokens.forEach(token ->
                token.setRevoked(true)
        );

        refreshTokenRepository.saveAll(tokens);
    }
}