package br.com.crm.beauty.web.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import br.com.crm.beauty.web.enums.Position;
import br.com.crm.beauty.web.models.Employee;
import br.com.crm.beauty.web.repositories.EmployeeRepository;
import br.com.crm.beauty.web.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.helpers.Helper;
import br.com.crm.beauty.web.dtos.CompanyDto;
import br.com.crm.beauty.web.exceptions.NotFoundException;
import br.com.crm.beauty.web.models.Company;
import br.com.crm.beauty.web.repositories.CompanyRepository;

@Service
public class CompanyService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final  EmployeeService employeeService;

    private final  UserService userService;


    private final ModelMapper modelMapper;

    public CompanyService(CompanyRepository companyRepository, EmployeeService employeeService, UserService userService, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.employeeService = employeeService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private Company toModel(CompanyDto company) {
        var model = modelMapper.map(company, Company.class);

        return model;
    }

    private CompanyDto toDTO(Company company) {
        var dto = modelMapper.map(company, CompanyDto.class);

        return dto;
    }

    private Company getById(UUID id) {
        var company = companyRepository.findById(id).orElseThrow(() -> {
            logger.warn("Company not found with id " + id);
            return new NotFoundException("Company not found with id " + id);
        });

        return company;

    }

    public CompanyDto findById(UUID id) {
        var company = getById(id);

        return toDTO(company);
    }

    public Page<CompanyDto> findAll(Pageable pageable) {
        // logger.info("Searching for all companies");

        Page<CompanyDto> itens = companyRepository.findAll(pageable)
                .map(this::toDTO);

        return itens;
    }

    public Company add(CompanyDto dto, String authMail) {
        logger.info("A company was registered {}", dto.getName());

        var user = userService.findByEmailOrThrow(authMail);

        if (user == null) {
            logger.warn("User not found for email: {}", authMail);
            throw new NotFoundException("User not found for email: " + authMail);
        }


        var slug = Helper.slugify(dto.getName()) + "-" + UUID.randomUUID().toString().substring(0, 8);


        var entity = toModel(dto);

        entity.setSlug(slug);
        entity.setCreatedAt(new Date());

        companyRepository.save(entity);

        var employee = new Employee();

        employee.setUser(user);
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setPosition(Position.OWNER);
        employee.setActive(true);
        employee.setCompany(entity);


        employeeService.create(employee);

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