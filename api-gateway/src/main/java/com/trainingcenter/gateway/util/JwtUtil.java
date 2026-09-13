package com.trainingcenter.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Component
public class JwtUtil {

    private final Key key;

    public JwtUtil(@Value("${app.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}") String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Object id = getAllClaimsFromToken(token).get("userId");
        if (id instanceof Number) {
            return ((Number) id).longValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
}
