package br.com.crm.beauty.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CompanyController.class);

    @GetMapping("")
    public String test(@RequestParam String param) {
        logger.info("test");
        logger.debug("test");
        logger.error("test");
        logger.warn("test");
       return "hello " + param;
    }
    
    
}
