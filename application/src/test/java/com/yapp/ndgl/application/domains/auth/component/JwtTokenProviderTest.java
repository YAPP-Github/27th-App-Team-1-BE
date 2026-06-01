package com.yapp.ndgl.application.domains.auth.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JwtTokenProvider")
class JwtTokenProviderTest {

    private static final String SECRET = "test-secret-key-for-unit-test-minimum-256-bits!!";
    private static final long EXPIRATION = 86400000L;

    private JwtTokenProvider jwtTokenProvider;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(SECRET, EXPIRATION);
        secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Nested
    @DisplayName("parseClaims()는")
    class ParseClaims {

        @Test
        @DisplayName("유효한 토큰이면 Claims를 반환한다")
        void 유효한_토큰이면_Claims를_반환한다() {
            final String token = jwtTokenProvider.generateToken("test-uuid");

            final Claims claims = jwtTokenProvider.parseClaims(token);

            assertThat(claims.getSubject()).isEqualTo("test-uuid");
        }

        @Nested
        @DisplayName("예외 케이스")
        class 예외_케이스 {

            @Test
            @DisplayName("만료된 토큰이면 ExpiredJwtException을 던진다")
            void 만료된_토큰이면_ExpiredJwtException을_던진다() {
                final String expiredToken = Jwts.builder()
                    .subject("test-uuid")
                    .issuedAt(new Date(System.currentTimeMillis() - 86400000L))
                    .expiration(new Date(System.currentTimeMillis() - 1000L))
                    .signWith(secretKey)
                    .compact();

                assertThatThrownBy(() -> jwtTokenProvider.parseClaims(expiredToken))
                    .isInstanceOf(ExpiredJwtException.class);
            }

            @Test
            @DisplayName("서명이 변조된 토큰이면 SignatureException을 던진다")
            void 서명이_변조된_토큰이면_SignatureException을_던진다() {
                final SecretKey wrongKey = Keys.hmacShaKeyFor(
                    "wrong-secret-key-minimum-256-bits-long!!!!!!".getBytes(StandardCharsets.UTF_8));
                final String tamperedToken = Jwts.builder()
                    .subject("test-uuid")
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .signWith(wrongKey)
                    .compact();

                assertThatThrownBy(() -> jwtTokenProvider.parseClaims(tamperedToken))
                    .isInstanceOf(SignatureException.class);
            }

            @Test
            @DisplayName("형식이 깨진 토큰이면 MalformedJwtException을 던진다")
            void 형식이_깨진_토큰이면_MalformedJwtException을_던진다() {
                final String malformedToken = "this.is.not.a.jwt";

                assertThatThrownBy(() -> jwtTokenProvider.parseClaims(malformedToken))
                    .isInstanceOf(MalformedJwtException.class);
            }

            @Test
            @DisplayName("서명 없는 토큰이면 UnsupportedJwtException을 던진다")
            void 서명_없는_토큰이면_UnsupportedJwtException을_던진다() {
                final String unsignedToken = Jwts.builder()
                    .subject("test-uuid")
                    .compact();

                assertThatThrownBy(() -> jwtTokenProvider.parseClaims(unsignedToken))
                    .isInstanceOf(UnsupportedJwtException.class);
            }

            @Test
            @DisplayName("빈 문자열이면 IllegalArgumentException을 던진다")
            void 빈_문자열이면_IllegalArgumentException을_던진다() {
                assertThatThrownBy(() -> jwtTokenProvider.parseClaims(""))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
