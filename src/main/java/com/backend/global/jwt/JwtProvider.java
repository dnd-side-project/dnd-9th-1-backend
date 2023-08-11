package com.backend.global.jwt;

import com.backend.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expired_time}")
    private int expiredTime;

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException e){
            throw new IllegalArgumentException("지원하지 않는 JWT입니다.");
        } catch (MalformedJwtException e){
            throw new IllegalArgumentException("잘못된 JWT 서명입니다.");
        } catch (SignatureException e){
            throw new IllegalArgumentException("토큰의 서명 유효성 검사가 실패하였습니다.");
        } catch (ExpiredJwtException e){
            throw new IllegalArgumentException("토큰의 유효기간이 만료되었습니다.");
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("토큰의 내용이 비어있습니다.");
        } catch (Exception e){
            throw new IllegalArgumentException("알 수 없는 토큰의 유효성 문제가 발생하였습니다.");
        }
    }
}
