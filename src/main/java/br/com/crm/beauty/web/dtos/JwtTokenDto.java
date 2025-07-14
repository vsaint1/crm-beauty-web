package br.com.crm.beauty.web.dtos;

import java.time.LocalDateTime;

public record JwtTokenDto(String token, LocalDateTime createdAt, LocalDateTime expiration) {

}