package edu.setokk.astrocluster.auth;

import edu.setokk.astrocluster.model.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils
{
    private final static Key secretKey;
    static {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(new SecureRandom());
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateJWT(UserDto user) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        Instant currentInstant = Instant.now();
        return Jwts.builder()
                .issuer("eclass")
                .subject(String.valueOf(user.getId()))
                .claims(claims)
                .issuedAt(Date.from(currentInstant))
                .expiration(Date.from(currentInstant.plus(Duration.ofMinutes(10))))
                .signWith(secretKey)
                .compact();
    }

    public static Claims extractAllClaims(String jwt) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
