package br.com.crm.beauty.web.repositories;

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
    @DisplayName("Should find company by slug")
    void findBySlugSuccess() {

        var company = createCompany("hello world");
        var result = companyRepository.findBySlug("hello-world");

        Assertions.assertThat(result).isSameAs(company);
    }

    @Test
    @DisplayName("Should fail to find company by slug")
    void findBySlugFail() {

        createCompany("Mercado Livre");
        var result = companyRepository.findBySlug("mercado-libre");

        Assertions.assertThat(result).isNull();
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
