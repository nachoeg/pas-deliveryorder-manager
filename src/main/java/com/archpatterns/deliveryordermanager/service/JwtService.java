package com.archpatterns.deliveryordermanager.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String SECRET = "clave-secreta-pqowieuyrrtgfdjhskslasmxcmncbcvsgsjwqkeuwim";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String[] extractRoles(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            var authorities = claims.get("authorities", List.class);
            if (authorities == null) {
                return new String[0];
            }
            return ((List<?>) authorities).stream()
                    .map(Object::toString)
                    .toArray(String[]::new);
        } catch (Exception ex) {
            log.error("Error: {}", ex.getMessage());
            return new String[0];
        }
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    public Long extractUserId(String token) {
    try {
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        var userId = claims.get("userId");
        if (userId != null) {
            return Long.valueOf(userId.toString());
        }
        return null; // o podés lanzar una excepción si preferís
    } catch (Exception ex) {
        log.error("Error al extraer userId del token: {}", ex.getMessage());
        return null;
    }
}


    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}