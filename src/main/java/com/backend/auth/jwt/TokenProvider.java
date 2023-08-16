package com.backend.auth.jwt;

import com.backend.auth.application.RefreshTokenService;
import com.backend.auth.domain.RefreshToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
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
        RefreshToken result =  refreshTokenService.findUidByRefreshToken(refreshToken);
        return generateAccessToken(result.getUid());
    }

    public String getPayload(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            throw e;
        } catch (UnsupportedJwtException e){
            throw e;
        } catch (IllegalArgumentException e){
            throw e;
        }
    }
}
