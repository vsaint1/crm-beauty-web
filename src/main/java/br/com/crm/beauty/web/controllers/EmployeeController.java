package br.com.crm.beauty.web.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.crm.beauty.web.dtos.EmployeeDto;
import br.com.crm.beauty.web.models.Employee;
import br.com.crm.beauty.web.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> searchByCompanyId(@RequestParam("company_id") UUID companyId,
            Pageable pageable) {
        var employees = employeeService.listByCompanyId(companyId, pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/search")
    public ResponseEntity<EmployeeDto> searchByQuery(@RequestParam("query") String query,
            @RequestParam("company_id") UUID companyId) {
        var employee = employeeService.findEmployeeQuery(query, companyId);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("register")
    public ResponseEntity<EmployeeDto> register(@RequestBody @Valid Employee employee) {
        var createdEmployee = employeeService.create(employee);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEmployee.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdEmployee);
    }

    @PutMapping("edit")
    public ResponseEntity<EmployeeDto> editEmployee( @RequestBody @Valid EmployeeDto employee) {
        var updated = employeeService.update(employee);

        return ResponseEntity.ok(updated);
    }

    // TODO: certify the request was made by user with permission to deactivate
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();

    }
}
