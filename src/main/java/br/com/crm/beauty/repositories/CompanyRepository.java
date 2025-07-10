package br.com.crm.beauty.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.crm.beauty.models.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    
}
