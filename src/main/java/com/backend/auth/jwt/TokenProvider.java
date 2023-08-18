package com.backend.auth.jwt;

import com.backend.auth.application.RefreshTokenService;
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

    private static final String TOKEN_HEADER_PREFIX = "Bearer ";

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
        return generateToken(uid, REFRESH_TOKEN_EXPIRE_TIME);
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
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getToken(String bearerToken) {
        if(bearerToken == null || !bearerToken.startsWith(TOKEN_HEADER_PREFIX)){
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        return bearerToken.substring(TOKEN_HEADER_PREFIX.length());
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken).getBody().getExpiration();
        Date now = new Date();
        return (expiration.getTime() - now.getTime());
    }
}
