package me.bttf.smartstore.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private final Key key;
    private final String issuer;
    private final long ttlMillis;

    public JwtUtil(String secret, String issuer, long ttlMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.issuer = issuer;
        this.ttlMillis = ttlMinutes * 60_000L;
    }

    public String createToken(String subject, Map<String,Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(ttlMillis)))
                .signWith(key)
                .compact();
    }
}
