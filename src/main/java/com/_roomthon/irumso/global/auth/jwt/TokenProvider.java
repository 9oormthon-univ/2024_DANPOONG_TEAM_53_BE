package com._roomthon.irumso.global.auth.jwt;

import com._roomthon.irumso.refreshToken.RefreshToken;
import com._roomthon.irumso.refreshToken.RefreshTokenRepository;
import com._roomthon.irumso.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final RefreshTokenProvider refreshTokenProvider;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT token 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getNickname()) // 닉네임을 subject로 사용
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // JWT token 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            // 로그 추가: 토큰 검증 시작
            System.out.println("Validating token: " + token);

            // JWT 토큰 검증
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())  // 비밀 키로 서명 검증
                    .parseClaimsJws(token);  // Claims 추출

            // 검증 성공 로그
            System.out.println("Token is valid!");

            return true;  // 토큰이 유효하면 true 반환
        } catch (Exception e) {
            // 예외 발생 시 로그
            System.out.println("Token validation failed: " + e.getMessage());
            return false;  // 토큰이 유효하지 않으면 false 반환
        }
    }

    // Token 기반으로 인증정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        System.out.println("Getting authentication for token: " + token);

        Claims claims = getClaims(token);  // 토큰에서 Claims 추출
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));  // 권한 설정

        // 로그 추가: Claims에서 사용자 정보 추출
        System.out.println("Extracted claims: " + claims);

        // Claims에서 사용자 정보로 User 객체 생성
        User user = getUserFromClaims(claims);

        // 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }


    private User getUserFromClaims(Claims claims) {
        // Claims에서 닉네임을 추출하고 User 객체 생성
        String nickname = claims.getSubject();
        return new User(nickname); // 닉네임 기반 User 생성
    }

    private Claims getClaims(String token) {
        try {
            System.out.println("Parsing token: " + token);
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())  // 비밀 키로 서명 검증
                    .parseClaimsJws(token)  // 토큰 파싱하여 Claims 반환
                    .getBody();
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
            throw new RuntimeException("Token parsing failed", e);
        }
    }


    public RefreshToken getStoredRefreshToken(String token) {
        // Access Token에 연결된 Refresh Token을 데이터베이스나 캐시에서 조회
        return refreshTokenRepository.findRefreshTokenByRefreshToken(token).orElse(null);
    }

    public boolean validRefreshToken(String refreshToken) {
        // Refresh Token이 유효한지 확인합니다. 예를 들어, 만료 여부를 확인 가능
        return refreshTokenProvider.isValid(refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        // 유효한 Refresh Token이 주어지면 새로운 Access Token을 생성하여 반환
        if (validRefreshToken(refreshToken)) {
            // Refresh Token에서 사용자 정보를 추출하여 새로운 Access Token을 생성
            String userId = refreshTokenProvider.getUserIdFromToken(refreshToken);
            return accessTokenProvider.createToken(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }
    }
}
