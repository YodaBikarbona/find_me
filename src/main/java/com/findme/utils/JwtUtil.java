package com.findme.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.exceptions.AuthorizationException;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private static final String AUTHORIZATION_ERROR_MESSAGE = "Authorization token is invalid!";

    public JwtUtil(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    // Public methods
    public String generateToken(UserEntity user, boolean accessToken) {
        long expirationMilliseconds;
        String secret;
        Map<String, Object> claims = getClaims(user);
        if (!accessToken) {
            expirationMilliseconds = ((((180L * 24) * 60) * 60) * 1000);
            secret = user.getSecurity().getRefreshTokenSecret();
        } else {
            expirationMilliseconds = (2 * 60 * 1000);
            secret = user.getSecurity().getAccessTokenSecret();
        }
        return Jwts.builder()
                .setSubject(user.getEmail())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMilliseconds))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserEntity user, boolean accessToken) {
        String secret = getTokenSecret(user, accessToken);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getSubject(String token, UserEntity user, boolean accessToken) {
        String secret = getTokenSecret(user, accessToken);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims getClaimsFromToken(String token, boolean accessToken) throws AuthorizationException {
        try {
            String[] parts = token.split("\\.");
            if (parts.length > 3) {
                throw new AuthorizationException(AUTHORIZATION_ERROR_MESSAGE);
            }
            String payload =  new String(TextCodec.BASE64URL.decode(parts[1]));
            Map<String, Object> claimsMap = objectMapper.readValue(payload, Map.class);
            Claims claims = Jwts.claims(claimsMap);
            Long userId = Long.parseLong(claims.get("id").toString());
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null || !validateToken(token, user, accessToken)) {
                throw new AuthorizationException(AUTHORIZATION_ERROR_MESSAGE);
            }
            return claims;
        } catch (Exception ex) {
            throw new AuthorizationException(AUTHORIZATION_ERROR_MESSAGE);
        }
    }

    // Private methods
    private String getTokenSecret(UserEntity user, boolean accessToken) {
        return accessToken ?  user.getSecurity().getAccessTokenSecret() : user.getSecurity().getRefreshTokenSecret();
    }

    private Map<String, Object> getClaims(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId().toString());
        claims.put("username", user.getUsername());
        return claims;
    }

}
