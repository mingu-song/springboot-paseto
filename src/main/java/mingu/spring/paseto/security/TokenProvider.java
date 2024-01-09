package mingu.spring.paseto.security;

import dev.paseto.jpaseto.*;
import dev.paseto.jpaseto.lang.Keys;
import lombok.extern.slf4j.Slf4j;
import mingu.spring.paseto.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private final SecretKey secretKey;
    private final KeyPair keyPair;

    public TokenProvider() {
        this.secretKey = Keys.secretKey();
        this.keyPair = Keys.keyPairFor(Version.V2);
    }

    // make token
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        User userPrincipal = (User) authentication.getPrincipal();

        return Pasetos.V2.LOCAL.builder()
                .setSharedSecret(secretKey)
                .setIssuedAt(now)
                .setExpiration(now.plus(2, ChronoUnit.HOURS))
                .setSubject(userPrincipal.getUsername())
                .setKeyId(UUID.randomUUID().toString())
                .setAudience("mingu.spring")
                .setIssuer("test")
                .claim("aut", authorities)
                .compact();
    }

    // filter token
    public boolean validateToken(String authToken) {
        try {
            parseToken(authToken);
            return !isTokenExpired(authToken);
        } catch (PasetoException e) {
            log.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    // filter token and get username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Instant extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Paseto parseToken(String token) {
        PasetoParser parser = Pasetos.parserBuilder()
                .setSharedSecret(secretKey)
                .setPublicKey(keyPair.getPublic())
                .build();

        return parser.parse(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(Instant.now());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims getClaims(String token) {
        return parseToken(token).getClaims();
    }
}
