package br.com.crm.beauty.web.controllers;

import java.security.Principal;
import java.util.UUID;

import br.com.crm.beauty.web.dtos.WorkingDayDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> searchByCompanyId(@RequestParam("company_id") UUID companyId,
                                                               Pageable pageable) {
        var employees = employeeService.listByCompanyId(companyId, pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("me")
    public ResponseEntity<EmployeeDto> getAuthenticatedUser(@RequestHeader("Authorization") String token) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(employeeService.findEmployeeByToken(token));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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

    @PreAuthorize("@authorizationService.canManageCompany(authentication)")
    @PutMapping("edit")
    public ResponseEntity<EmployeeDto> editEmployee(@RequestBody @Valid EmployeeDto employee) {
        var updated = employeeService.update(employee);

        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("@authorizationService.canManageCompany(authentication)")
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("add-working-day")
    public ResponseEntity<EmployeeDto> registerWorkingDays(@RequestBody @Valid WorkingDayDto workingDayDto) {


        var updatedEmployee = employeeService.addWorkingDays(workingDayDto);

        return ResponseEntity.ok(updatedEmployee);
    }

}
