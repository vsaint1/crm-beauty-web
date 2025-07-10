package br.com.crm.beauty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CrmBeautyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmBeautyApplication.class, args);

	}

}
