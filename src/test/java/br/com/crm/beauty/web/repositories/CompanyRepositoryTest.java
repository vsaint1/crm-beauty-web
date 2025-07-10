package br.com.crm.beauty.web.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.crm.beauty.helpers.Helper;
import br.com.crm.beauty.web.models.Company;
import jakarta.persistence.EntityManager;
import net.bytebuddy.utility.RandomString;

@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    void findBySlug_WhenExists_ReturnCompany() {

        var company = createCompany("hello world");
        var result = companyRepository.findBySlug("hello-world");

        Assertions.assertThat(result).isSameAs(company);
    }

    @Test
    void findBySlug_WhenNotExists_ReturnNull() {

        createCompany("Mercado Livre");
        var result = companyRepository.findBySlug("mercado-libre");

        assertNull(result);
    }

    private Company createCompany(String name) {

        var company = new Company();

        company.setName(name);
        var slug = Helper.slugify(name);
        company.setSlug(slug);

        company.setCnpj(RandomString.make(14));

        company.setDescription("lorem ipsum");
        company.setLogoUrl("logo");
        company.setPrimaryColor("#fff");
        company.setSecondaryColor("#fafafa");
        company.setIsActive(true);

        entityManager.persist(company);

        return company;

    }
}
