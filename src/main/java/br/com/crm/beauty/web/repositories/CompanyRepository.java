package br.com.crm.beauty.web.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.crm.beauty.web.models.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("select c  from Company c where c.slug = :slug")
    public Company findBySlug(String slug);
}
