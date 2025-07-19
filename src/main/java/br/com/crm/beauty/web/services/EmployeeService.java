package br.com.crm.beauty.web.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.dtos.EmployeeDto;
import br.com.crm.beauty.web.exceptions.NotFoundException;
import br.com.crm.beauty.web.models.Company;
import br.com.crm.beauty.web.models.Employee;
import br.com.crm.beauty.web.models.User;
import br.com.crm.beauty.web.repositories.CompanyRepository;
import br.com.crm.beauty.web.repositories.EmployeeRepository;
import br.com.crm.beauty.web.repositories.UserRepository;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private JwtService jwtService;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeService.class);

    private ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository,
            UserRepository userRepository, JwtService jwtService, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    public EmployeeDto create(Employee employee) {
        Company company = companyRepository.findById(employee.getCompany().getId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        User user = userRepository.findById(employee.getUser().getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        employee.setCompany(company);
        employee.setUser(user);

        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);

        logger.info(String.format("Employee was registered with user_id(%d) on company_id(%s):", employee.getId(),
                employee.getCompany().getId()));

        var employeeDto = modelMapper.map(employee, EmployeeDto.class);

        return employeeDto;

    }

    public void deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        employee.setActive(false);
        employee.setUpdatedAt(LocalDateTime.now());

        logger.info(String.format("Employee was deactivated with user_id(%d) from company_id(%s):", employee.getId(),
                employee.getCompany().getId()));

        employeeRepository.save(employee);
    }

    public EmployeeDto findEmployeeByToken(String token) {

        var subject = jwtService.decodeSubject(token);

        // Optional<User> userOptional = userRepository.findByEmail(subject);

        // if (userOptional.isEmpty()) {
        //     logger.warn("User not found for email: " + subject);
        //     throw new NotFoundException("User not found");
        // }

        Employee employee = employeeRepository.findActiveEmployeeWithUserAndCompanyByUserEmail(subject);

        if (employee == null) {
            logger.warn("Employee not found for user email: " + subject);
            throw new NotFoundException("Employee data not found");
        }

        return modelMapper.map(employee, EmployeeDto.class);
    }

    public EmployeeDto update(EmployeeDto employee) {

        Employee existingEmployee = employeeRepository.findByUserIdAndCompanyId(employee.getUser().getId(),
                employee.getCompany().getId());

        if (existingEmployee == null) {
            logger.warn("Employee not found with user id: " + employee.getUser().getId() + " and company id: "
                    + employee.getCompany().getId());
            throw new NotFoundException("Employee not found");
        }

        // TODO: only admin can change company and user
        // existingEmployee.setCompany(employee.getCompany());
        // existingEmployee.setUser(employee.getUser());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setActive(employee.isActive());
        existingEmployee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(existingEmployee);

        return modelMapper.map(existingEmployee, EmployeeDto.class);
    }

    public EmployeeDto findEmployeeQuery(String query, UUID companyId) {
        var employee = employeeRepository.findByCompanyIdAndQuery(companyId, query);

        if (employee == null) {
            logger.warn("Employee not found with query: " + query + " for company id: " + companyId);
            throw new NotFoundException("Employee not found");
        }

        return modelMapper.map(employee, EmployeeDto.class);
    }

    public Page<EmployeeDto> listByCompanyId(UUID companyId, Pageable pageable) {
        var employees = employeeRepository.findByCompanyIdPaged(companyId, pageable);

        return employees.map(employee -> modelMapper.map(employee, EmployeeDto.class));
    }

    public Employee findByEmail(String name) {
       var employee = employeeRepository.findByUserEmail(name);

        if (employee == null) {
            logger.warn("Employee not found with email: " + name);
            throw new NotFoundException("Employee not found");
        }

        return employee;
    }

}
