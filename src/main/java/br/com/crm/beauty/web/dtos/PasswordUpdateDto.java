package br.com.crm.beauty.web.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record PasswordUpdateDto(@NotEmpty String oldPassword, @NotEmpty @Min(6) String newPassword,
        @NotEmpty @Min(6) String confirmPassword) {

}
