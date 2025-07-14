package br.com.crm.beauty.web.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeService.class);

    private ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository,
            UserRepository userRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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

        var employeeDto = modelMapper.map(employee, EmployeeDto.class);

        return employeeDto;

    }

    public void deactivateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        employee.setActive(false);
        employee.setUpdatedAt(LocalDateTime.now());

        employeeRepository.save(employee);
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

}
