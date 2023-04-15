package com.integrador.svfapi.Utils;

import com.integrador.svfapi.Classes.Student;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private final Key key;

    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Student student) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(student.getStudentCod())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, Student student) {
        final String username = extractUsername(token);
        return (username.equals(student.getStudentCod()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public Date extractExpiration(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}

