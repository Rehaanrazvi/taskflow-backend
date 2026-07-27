package com.TaskFlow.TF.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.function.Function;

import java.security.Key;


@Component
public class JwtUtil {

    // 1. SECRET: Must be at least 32 chars for HS256.
    // In production, store this in environment variables!
    private static final String SECRET = "MySuperSecretKeyForJWTThatIsAtLeast32CharactersLong!";

    // 2. Generate a signing key from the secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 3. PUBLIC METHOD: Generate a token for a given username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                    // Who is this for? -> "john"
                .setIssuedAt(new Date())                 // When was it created?
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Valid for 10 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign it
                .compact();                              // Build the final string
    }
    // 1. Extract the username (subject) from the JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Check if the token is expired
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // 3. Validate the token against the username
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // 4. A generic helper to extract any claim (Used by the above methods)
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}