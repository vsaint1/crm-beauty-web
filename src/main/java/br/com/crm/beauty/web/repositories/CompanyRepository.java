package br.com.crm.beauty.web.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.crm.beauty.web.models.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    
}
