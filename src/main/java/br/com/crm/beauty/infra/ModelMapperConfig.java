package br.com.crm.beauty.infra;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        var config = mapper.getConfiguration();
        config.setSkipNullEnabled(true);

        return mapper;
    }
}
