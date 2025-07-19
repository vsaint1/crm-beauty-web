package br.com.crm.beauty.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.crm.beauty.web.dtos.CompanyDto;
import br.com.crm.beauty.web.models.Company;
import br.com.crm.beauty.web.services.CompanyService;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<Page<CompanyDto>> listAllPaged(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {


        var result = companyService.findAll(pageable);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<CompanyDto> findById(@PathVariable UUID id) {

        var company = companyService.findById(id);

        return ResponseEntity.ok().body(company);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable UUID id, @RequestBody Company company) {

        var updated = companyService.update(id, company);

        return ResponseEntity.ok().body(updated);
    }

    @PatchMapping("change-status/{id}")
    public ResponseEntity<Company> changeStatus(@PathVariable UUID id) {

        companyService.updateActive(id);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("create")
    public ResponseEntity<Company> register(@RequestBody @Validated CompanyDto entity) {

        var created = companyService.add(entity);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(uri).body(created);
    }

}
