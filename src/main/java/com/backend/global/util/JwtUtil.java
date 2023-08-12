package com.backend.global.util;

import com.backend.user.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static String generateToken(User user, String key, Long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
