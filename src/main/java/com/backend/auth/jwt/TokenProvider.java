package com.backend.auth.jwt;

import com.backend.auth.application.RefreshTokenService;
import com.backend.auth.jwt.exception.InvalidJwtException;
import com.backend.auth.jwt.exception.JwtExpiredException;
import com.backend.auth.jwt.exception.NullJwtException;
import com.backend.global.common.code.ErrorCode;
import com.backend.member.domain.Member;
import com.backend.member.domain.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class TokenProvider {
     private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; // 2시간

   // private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60; // 1분

     private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; // 2주

  //  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60; // 1분
    private static final String TOKEN_HEADER_PREFIX = "Bearer ";

    private static final String AUTHORITIES_KEY = "auth";

    private final Key key;

    private final MemberRepository memberRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenService refreshTokenService, MemberRepository memberRepository){
        this.memberRepository = memberRepository;
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
        Member member = memberRepository.getByUid(uid);
        Date now = new Date();
        return Jwts.builder()
                .setSubject(uid)
                .claim(AUTHORITIES_KEY, member.getRole())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPayload(String token){
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            log.info("토큰의 기한이 만료되었습니다.");
            throw new JwtExpiredException(ErrorCode.TOKEN_EXPIRED);
        } catch (SecurityException e) {
            log.info("잘못된 토큰 시그니처입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 형식의 토큰입니다.");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.info("유효하지 않은 토큰입니다. ");
        }
    }

    public String getToken(String bearerToken) {
        if(Objects.isNull(bearerToken) || bearerToken.isEmpty()){
            throw new NullJwtException(ErrorCode.NO_TOKEN_PROVIDED);
        } else if (!bearerToken.startsWith(TOKEN_HEADER_PREFIX)){
            throw new InvalidJwtException(ErrorCode.INVALID_TOKEN);
        }
        return bearerToken.substring(TOKEN_HEADER_PREFIX.length());
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken).getBody().getExpiration();
        Date now = new Date();
        return (expiration.getTime() - now.getTime());
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaims(accessToken);

        // 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // UserDetails 객체에서 Authentication 반환
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
