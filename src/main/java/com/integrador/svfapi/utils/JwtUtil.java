package com.integrador.svfapi.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private Key key;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String code, long tokenDuration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("code", code);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenDuration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenValidationResult validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            String code = (String) claims.getBody().get("code");
            if (!isTokenExpired(token)) {
                if (CodeValidator.isAdminCode(code)) {
                    return new TokenValidationResult(true, code, TokenType.ADMIN);
                } else if (CodeValidator.isStudentCode(code)) {
                    return new TokenValidationResult(true, code, TokenType.STUDENT);
                }
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Token inválido");
        }
        return new TokenValidationResult(false, null, null);
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody().getExpiration();
    }
}


