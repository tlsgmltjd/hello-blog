package com.blog.helloblog.service;

import com.blog.helloblog.config.jwt.TokenProvider;
import com.blog.helloblog.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserSerivce userSerivce;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사, 실패시 예외
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userSerivce.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
