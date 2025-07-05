package org.unibl.etf.fitsocial.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unibl.etf.fitsocial.auth.user.UserDto;
import org.unibl.etf.fitsocial.auth.user.UserMapper;
import org.unibl.etf.fitsocial.auth.user.UserRepository;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long expirationMs;
    private final String issuer;
    private final long refreshExpirationMs;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs}") long expirationMs,
            @Value("${jwt.issuer}")  String issuer,
            @Value("${refresh.expirationMs}")  long refreshExpirationMs,
            UserRepository userRepository, UserMapper userMapper) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.issuer = issuer;
        this.refreshExpirationMs = refreshExpirationMs;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public TokenResponse generateTokens(String username) {
        var token = Jwts.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
        var refreshToken = Jwts.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(secretKey)
                .compact();

        return new TokenResponse(token, refreshToken, userMapper.toDto(userRepository.findByUsernameAndDeletedAtIsNull(username)));
    }

    public TokenResponse refreshTokens(String refreshToken) {
        var tokenUsr = extractUsername(refreshToken);
        var user = userRepository.findByUsernameAndDeletedAtIsNull(tokenUsr);
        var username = user.getUsername();

        return validateToken(refreshToken, username) ? generateTokens(username) : new TokenResponse("", "", null);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        final String usernameFromToken = extractUsername(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    public record TokenResponse(String token, String refreshToken, UserDto user) {}
}
