package br.com.crm.beauty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = CrmBeautyApplication.class)
@ActiveProfiles("test")
class CrmBeautyApplicationTests {

	@Test
	void contextLoads() {
	}

}
