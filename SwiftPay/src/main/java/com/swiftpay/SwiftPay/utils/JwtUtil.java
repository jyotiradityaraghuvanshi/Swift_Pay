package com.swiftpay.SwiftPay.utils;

import com.swiftpay.SwiftPay.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static long EXPIRATION_TIME;
    @Value("${jwt.token.expiration}") // this annotation does not work with static, so we use setter method
    public void setExpirationTime(long expirationTime) {
        JwtUtil.EXPIRATION_TIME = expirationTime;
    }

    public static String generateToken(String subject , Role role){
        return Jwts.builder()
                .setSubject(subject)
                .claim("role" , role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 180000 + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public static String validateToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // extracting the role from token.
    public static String extractUserRole(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role" , String.class);
    }

}
