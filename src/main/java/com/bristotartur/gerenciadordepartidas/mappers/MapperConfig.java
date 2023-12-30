package com.bristotartur.gerenciadordepartidas.mappers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public TeamMapper teamMapper() {
        return TeamMapper.INSTANCE;
    }
}
