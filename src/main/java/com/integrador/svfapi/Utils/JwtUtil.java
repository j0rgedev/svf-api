package com.integrador.svfapi.Utils;

import com.integrador.svfapi.Classes.Student;
import com.integrador.svfapi.Classes.StudentLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {


    private final Key key;

    public JwtUtil(@Value("${jwt.secret}")
                   String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        System.out.println("la key secret "+key);
    }

    public String generateToken(StudentLogin studentLogin) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(studentLogin.getStudentCod())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, StudentLogin studentLogin) {
        final String username = extractUsername(token);
        return (username.equals(studentLogin.getStudentCod()) && !isTokenExpired(token));
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

