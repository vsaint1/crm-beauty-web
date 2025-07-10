package br.com.crm.beauty.web.dtos;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyDto(

        UUID id,

        String name,
        
        String slug,

        @Size(max = 512) String logoUrl,

        @Size(max = 50) String primaryColor,

        @Size(max = 50) String secondaryColor,

        @Size(max = 500) String description,

        boolean isActive,

        Date createdAt,

        @NotBlank(message = "CNPJ is required") String cnpj

) {

}
