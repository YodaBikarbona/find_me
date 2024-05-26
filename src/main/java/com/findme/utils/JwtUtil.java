package com.findme.utils;

import com.findme.exceptions.AuthorizationException;
import com.findme.user.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // Public methods
    public String generateToken(UserEntity user, boolean accessToken) {
        long expirationMilliseconds;
        String secret;
        Map<String, Object> claims = getClaims(user);
        if (!accessToken) {
            expirationMilliseconds = ((((180L * 24) * 60) * 60) * 1000);
            secret = user.getSecurity().getRefreshTokenSecret();
        } else {
            expirationMilliseconds = (5 * 60 * 1000);
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
        } catch (Exception e) {
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

    public Claims getClaimsFromToken(String token, UserEntity user, boolean accessToken) throws AuthorizationException {
        Claims claims = Jwts.parserBuilder()
                .build()
                .parseClaimsJws(token)
                .getBody();
        if (!validateToken(token, user, accessToken)) {
            throw new AuthorizationException("The token is invalid!");
        }
        return claims;
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
