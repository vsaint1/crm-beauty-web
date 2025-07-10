package br.com.crm.beauty.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.helpers.Helper;
import br.com.crm.beauty.models.Company;
import br.com.crm.beauty.repositories.CompanyRepository;

@Service
public class CompanyService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company findById(UUID id) {
        var company = companyRepository.findById(id).orElseThrow(() -> {
            logger.warn("Company not found with id " + id);
            return new RuntimeException("Company not found with id " + id);
        });

        return company;

    }

    public Page<Company> findAll(Pageable pageable) {
        logger.info("Searching for all companies");
        return companyRepository.findAll(pageable);
    }

    public Company add(Company company) {
        logger.info("A company was registered " + company.getName());
        var slug = Helper.slugify(company.getName());
        company.setSlug(slug);

        return companyRepository.save(company);
    }

    public Company update(UUID id, Company company) {
        var entity = findById(id);

        if (entity != null) {
            var slug = Helper.slugify(entity.getName());
            entity.setName(company.getName());
            entity.setSlug(slug);
            entity.setLogoUrl(company.getLogoUrl());
            entity.setPrimaryColor(company.getPrimaryColor());
            entity.setSecondaryColor(company.getSecondaryColor());
            entity.setDescription(company.getDescription());
            entity.setCnpj(company.getCnpj());

            return companyRepository.save(entity);
        }

        return entity;
    }

    public void updateActive(UUID id) {
        var entity = findById(id);
        var status = entity.isIsActive();
        entity.setIsActive(!status);

        companyRepository.save(entity);

    }

    public void delete(UUID id) {
        companyRepository.deleteById(id);
    }

}