package br.com.crm.beauty.web.services;

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

    public CompanyDto add(Company company) {
        logger.info("A company was registered " + company.getName());
        var slug = Helper.slugify(company.getName());
        company.setSlug(slug);
        companyRepository.save(company);

        return this.toDTO(company);
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