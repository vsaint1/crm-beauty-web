package br.com.crm.beauty.web.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.dtos.JwtTokenDto;
import br.com.crm.beauty.web.dtos.UserDto;

@Service
public class JwtService {

    private JwtEncoder jwtEncoder;

    private final long EXPIRATION_TIME = 36000L; // 10 hours in seconds

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public JwtTokenDto generate(UserDto user) {

        var createdAt = LocalDateTime.now();
        var expiration = createdAt.plusSeconds(EXPIRATION_TIME);

        var claims = JwtClaimsSet.builder()
                .issuer("crm_beautysalon")
                .issuedAt(createdAt.toInstant(ZoneOffset.UTC))
                .expiresAt(expiration.toInstant(ZoneOffset.UTC))
                .subject(user.getEmail())
                .claim("roles", user.getRoles()).build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        return new JwtTokenDto(jwt.getTokenValue(), createdAt, expiration);

    }

}
