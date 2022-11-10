package com.feelmycode.parabole.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtils {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public Claims extractAllClaims(String token) {
        if (StringUtils.isEmpty(token)) return null;
        String secretKeyEncodeBase64 = Encoders.BASE64.encode(secretKey.getBytes());
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(secretKeyEncodeBase64).build()
                .parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            claims = null;
        }
        return claims;
    }

    public String extractUsername(String token) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        else return claims.get("username", String.class);
    }

    public String extractRole(String token) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        else return claims.get("role", String.class);
    }

    public Long extractSellerId(String token) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        else return claims.get("sellerId", Long.class);
    }

    public String extractEmail(String token) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        else return claims.get("email", String.class);
    }

    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        else return claims.get("userId", Long.class);
    }

}
