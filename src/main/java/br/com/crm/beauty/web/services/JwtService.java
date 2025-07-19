package br.com.crm.beauty.web.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.dtos.JwtTokenDto;
import br.com.crm.beauty.web.dtos.UserDto;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final long EXPIRATION_TIME = 36000L; // 10 hours in seconds

    public JwtService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public JwtTokenDto generate(UserDto user) {

        var createdAt = LocalDateTime.now();
        var expiration = createdAt.plusSeconds(EXPIRATION_TIME);

        var claims = JwtClaimsSet.builder()
                .issuer("crm_beautysalon")
                .issuedAt(createdAt.toInstant(ZoneOffset.UTC))
                .expiresAt(expiration.toInstant(ZoneOffset.UTC))
                .subject(user.getEmail())
                .claim("id", user.getId())
                .claim("roles", user.getRoles()).build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        return new JwtTokenDto(jwt.getTokenValue(), createdAt, expiration);

    }

    public String decodeSubject(String token) {
        Jwt jwt = jwtDecoder.decode(token.substring(7));
        var email = jwt.getSubject();
        return email;
    }

    public JwtTokenDto refreshToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String email = jwt.getSubject();
        List<String> roles = jwt.getClaimAsStringList("roles");

        var createdAt = LocalDateTime.now();
        var expiration = createdAt.plusSeconds(EXPIRATION_TIME);

        var claims = JwtClaimsSet.builder()
                .issuer("crm_beautysalon")
                .issuedAt(createdAt.toInstant(ZoneOffset.UTC))
                .expiresAt(expiration.toInstant(ZoneOffset.UTC))
                .subject(email)
                .claim("roles", roles)
                .build();

        var newJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));

        return new JwtTokenDto(newJwt.getTokenValue(), createdAt, expiration);
    }

}
