package br.com.crm.beauty.web.services;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.helpers.Helper;
import br.com.crm.beauty.web.dtos.CompanyDto;
import br.com.crm.beauty.web.models.Company;
import br.com.crm.beauty.web.repositories.CompanyRepository;

@Service
public class CompanyService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private Company getById(UUID id) {
        var company = companyRepository.findById(id).orElseThrow(() -> {
            logger.warn("Company not found with id " + id);
            return new RuntimeException("Company not found with id " + id);
        });

        return company;

    }

    public CompanyDto findById(UUID id) {
        var company = getById(id);

        return toDTO(company);
    }

    private Company toModel(CompanyDto company) {
        var model = new Company();
        model.setId(company.id());
        model.setName(company.name());
        model.setSlug(company.slug());
        model.setLogoUrl(company.logoUrl());
        model.setPrimaryColor(company.primaryColor());
        model.setSecondaryColor(company.secondaryColor());
        model.setDescription(company.description());
        model.setIsActive(company.isActive());
        model.setCreatedAt(company.createdAt());
        model.setCnpj(company.cnpj());

        return model;
    }

    private CompanyDto toDTO(Company company) {
        var dto = new CompanyDto(
                company.getId(),
                company.getName(),
                company.getSlug(),
                company.getLogoUrl(),
                company.getPrimaryColor(),
                company.getSecondaryColor(),
                company.getDescription(),
                company.isActive(),
                company.getCreatedAt(),
                company.getCnpj());

        return dto;
    }

    public Page<CompanyDto> findAll(Pageable pageable) {
        logger.info("Searching for all companies");

        Page<CompanyDto> itens = companyRepository.findAll(pageable)
                .map(this::toDTO);

        return itens;
    }

    public Company add(CompanyDto dto) {
        logger.info("A company was registered " + dto.name());

        var slug = Helper.slugify(dto.name());

        var entity = toModel(dto);
        
        entity.setSlug(slug);
        entity.setCreatedAt(new Date());

        companyRepository.save(entity);

        return entity;
    }

    public CompanyDto update(UUID id, Company company) {
        var entity = getById(id);

        if (entity != null) {
            var slug = Helper.slugify(entity.getName());
            entity.setName(company.getName());
            entity.setSlug(slug);
            entity.setLogoUrl(company.getLogoUrl());
            entity.setPrimaryColor(company.getPrimaryColor());
            entity.setSecondaryColor(company.getSecondaryColor());
            entity.setDescription(company.getDescription());
            entity.setCnpj(company.getCnpj());

            companyRepository.save(entity);
            return this.toDTO(entity);
        }

        return this.toDTO(entity);
    }

    public void updateActive(UUID id) {
        var entity = getById(id);
        var status = entity.isActive();
        entity.setIsActive(!status);

        companyRepository.save(entity);

    }

    public void delete(UUID id) {
        companyRepository.deleteById(id);
    }

}