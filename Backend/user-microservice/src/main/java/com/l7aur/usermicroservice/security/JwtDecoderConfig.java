package com.l7aur.usermicroservice.security;

import com.nimbusds.jwt.JWTParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.Map;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            try {
                var parsedJwt = JWTParser.parse(token);

                var header = parsedJwt.getHeader().toJSONObject();
                var claims = parsedJwt.getJWTClaimsSet().getClaims();

                Jwt.Builder jwtBuilder = Jwt.withTokenValue(token)
                        .headers(h -> h.putAll(header));

                // Convert each claim to proper type
                for (Map.Entry<String, Object> entry : claims.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof java.util.Date dateValue) {
                        jwtBuilder.claim(key, Instant.ofEpochMilli(dateValue.getTime()));
                    } else {
                        jwtBuilder.claim(key, value);
                    }
                }

                return jwtBuilder.build();
            } catch (Exception e) {
                throw new JwtException("Failed to decode token", e);
            }
        };
    }
}
