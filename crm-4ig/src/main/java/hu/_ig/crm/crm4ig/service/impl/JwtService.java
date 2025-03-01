package hu._ig.crm.crm4ig.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Consumer<Map<String, Object>> claimsConsumer = claims -> {
            claims.put("sub", authentication.getName());
            claims.put("roles", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList());
        };
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                //.issuer("https://issuer.example.com") //Kibocsátó: A JWT-t kiállító entitás.
                .issuedAt(now) //IssuedAt: A JWT kiadásának időpontja.
                .expiresAt(now.plus(1, ChronoUnit.HOURS))//ExpiresAt: Az a lejárati idő, amely után a JWT már nem érvényes.
                .subject(authentication.getName()) //Tárgy: A JWT tárgya (általában felhasználói azonosító).
                .claims(claimsConsumer) //Egyéni követelések: Bármely további követelés, amelyet fel kíván venni.
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
