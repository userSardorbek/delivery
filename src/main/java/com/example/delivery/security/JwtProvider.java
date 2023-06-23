package com.example.delivery.security;

import com.example.delivery.entity.Role;
import com.example.delivery.payload.LogInDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    public static final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;
    public static final String KEY = "BUmeningkalitso'zim";

    public String generateToken(String username, Role role) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", role)
                .signWith(SignatureAlgorithm.HS512, KEY)
                .compact();
    }

    public String getUsername(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
