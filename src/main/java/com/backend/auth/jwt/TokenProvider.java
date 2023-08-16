package com.backend.auth.jwt;

import com.backend.auth.application.RefreshTokenService;
import com.backend.auth.domain.RefreshToken;
import com.backend.global.common.code.ErrorCode;
import com.backend.global.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    private final RefreshTokenService refreshTokenService;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenService refreshTokenService){
        this.refreshTokenService = refreshTokenService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String uid){
        return generateToken(uid, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generateRefreshToken(String uid){
        String refreshToken = generateToken(uid, REFRESH_TOKEN_EXPIRE_TIME);
        refreshTokenService.saveRefreshToken(refreshToken, uid);
        return refreshToken;
    }

    public String generateToken(String uid, Long expireTime){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(uid)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String reissueAccessToken(String refreshToken) {
        String uid =  refreshTokenService.findUidByRefreshToken(refreshToken);
        return generateAccessToken(uid);
    }

    public String getPayload(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(String token) throws Exception {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){
            throw new Exception("잘못된 형식의 토큰입니다.");
        }
    }
}
