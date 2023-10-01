package com.blog.helloblog.config.jwt;

import com.blog.helloblog.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToekn(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // 토큰을 생성하는 메서드, 만료시간, 유저정보를 인자로 받음
    // 헤더에 타입, 내용에 발급자, 발급일시, 만료일시, 토큰 제목이 클레임에는 유저 ID를 저장. 토큰을 만들때는 프로퍼티즈 파일에 선언해둔 비밀키 값과 함께 HS256 방식으로 암호화
    private String makeToekn(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 토큰이 유효한지 검사하는 메서드. 비밀키 값으로 토큰 복호화를 진행한다.
    // 복호화 과정에서 에러가 발생하면 false, 아무 에러가 없으면 true를 반환
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀키 값으로 복호화
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 인증 정보를 담은 객체인 Authentication를 반환하는 메서드.
    // 비밀키 값으로 토큰을 복호화 한 뒤 클레임을 가져오는 getClaims() 메서드로 클레임 정보를 받아 사용자 이메일이 들어있는 sub와 토큰 기반으로 인증 정보를 생성한다.
    // UsernamePasswordAuthenticationToken 의 첫 인자로 들어가는 User는 스프링 시큐리티에서 제공하는 객체인 User 클래스를 임포트 해야한다.
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),
                "", authorities), token, authorities);
    }

    // 토큰 기반으로 사용자 Id를 가져오는 메서드이다. getClaims() 메서드로 클레임 정보를 가져와 id로 저장된 값을 가져와 반환.
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
