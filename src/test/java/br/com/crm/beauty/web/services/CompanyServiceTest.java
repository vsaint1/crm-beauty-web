package br.com.crm.beauty.web.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.crm.beauty.helpers.Helper;
import br.com.crm.beauty.web.dtos.CompanyDto;
import br.com.crm.beauty.web.exceptions.NotFoundException;
import br.com.crm.beauty.web.models.Company;
import br.com.crm.beauty.web.repositories.CompanyRepository;
import net.bytebuddy.utility.RandomString;

public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void shouldReturnCompanyDtoWhenExists() {
        var company = new Company();

        var uuid = UUID.randomUUID();

        company.setId(uuid);
        company.setName("world trade company");
        company.setSlug("world-trade-company");
        company.setCnpj("123");
        company.setDescription("desc");
        company.setLogoUrl("logo");
        company.setPrimaryColor("#fff");
        company.setSecondaryColor("#fafafa");
        company.setIsActive(true);

        when(companyRepository.findById(any())).thenReturn(Optional.of(company));

        var companyDto = new CompanyDto();
        companyDto.setId(uuid);
        companyDto.setName("world trade company");

        when(modelMapper.map(company, CompanyDto.class)).thenReturn(companyDto);

        var result = companyService.findById(uuid);

        assertEquals(company.getId(), result.getId());

        verify(companyRepository, times(1)).findById(company.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDoesNotExists() {

        when(companyRepository.findById(any())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> companyService.findById(UUID.randomUUID()));
    }

}
