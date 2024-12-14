package com.toy.tripstory.auth;

import com.toy.tripstory.auth.constance.GrantType;
import com.toy.tripstory.auth.constance.TokenType;
import com.toy.tripstory.error.ErrorType;
import com.toy.tripstory.error.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    // 액세스 토큰의 만료 시간 (밀리초 단위)
    private final String accessTokenExpirationTime;
    // 리프레시 토큰의 만료 시간 (밀리초 단위)
    private final String refreshTokenExpirationTime;
    // JWT 토큰을 생성 및 검증할 때 사용하는 비밀 키
    private final String tokenSecret;

    private final static String MEMBER_ID = "memberId";
    private final static String ROLES = "roles";

    public JwtToken createJwtTokenDto(Long memberId, String roles) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(memberId, roles, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, roles, refreshTokenExpireTime);

        return JwtToken.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    /**
     * 액세스 토큰의 만료 시간을 생성합니다.
     *
     * @return 생성된 액세스 토큰의 만료 시간
     */
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    /**
     * 리프레시 토큰의 만료 시간을 생성합니다.
     *
     * @return 생성된 리프레시 토큰의 만료 시간
     */
    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    /**
     * 주어진 사용자 ID, 역할, 만료 시간을 바탕으로 액세스 토큰을 생성합니다.
     *
     * @param memberId 사용자 ID
     * @param roles    사용자 역할
     * @param expirationTime 토큰 만료 시간
     * @return 생성된 액세스 토큰
     */
    public String createAccessToken(Long memberId, String roles, Date expirationTime) {
        // 액세스 토큰에 사용자의 역할 정보를 포함시키는 이유:
        // 사용자의 권한(roles)을 액세스 토큰에 포함시킴으로써, 서버는 토큰만으로도 사용자의 권한을 확인할 수 있습니다.
        // 이렇게 하면, 매 요청마다 데이터베이스에서 사용자의 권한을 조회할 필요가 없어져 성능이 향상됩니다.
        return createToken(TokenType.ACCESS.name(), memberId, roles, expirationTime);
    }

    /**
     * 주어진 사용자 ID와 만료 시간을 바탕으로 리프레시 토큰을 생성합니다.
     *
     * @param memberId 사용자 ID
     * @param expirationTime 토큰 만료 시간
     * @return 생성된 리프레시 토큰
     */
    public String createRefreshToken(Long memberId, String roles, Date expirationTime) {
        return createToken(TokenType.REFRESH.name(), memberId, roles, expirationTime);

    }

    private String createToken(String subject, Long memberId, String roles, Date expirationTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .claim(MEMBER_ID, memberId)
                .claim(ROLES, roles)
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);  // 토큰을 파싱하고 검증
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorType.TOKEN_EXPIRED);  // 만료 예외 발생
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);  // 유효하지 않은 토큰인 경우 로그 기록
            throw new UnauthorizedException(ErrorType.NOT_VALID_TOKEN);  // 유효하지 않은 토큰 예외 발생
        }
    }


    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();  // 토큰에서 클레임을 파싱하고 추출
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);  // 유효하지 않은 토큰인 경우 로그 기록
            throw new UnauthorizedException(ErrorType.NOT_VALID_TOKEN);  // 유효하지 않은 토큰 예외 발생
        }
        return claims;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);  // 토큰에서 클레임 추출

        Long memberId = claims.get(MEMBER_ID, Long.class);
        if(memberId == null) {
            throw new RuntimeException("토큰의 사용자의 ID 정보가 없습니다.");
        }

        List<String> roles = Arrays.stream(claims.get(ROLES, String.class).split(",")).toList();
        if(roles.isEmpty()) {
            throw new RuntimeException("토큰의 사용자의 역할 정보가 없습니다.");
        }

        Collection<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String subject = claims.getSubject();

        UserDetails principal = new User(subject, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


}
