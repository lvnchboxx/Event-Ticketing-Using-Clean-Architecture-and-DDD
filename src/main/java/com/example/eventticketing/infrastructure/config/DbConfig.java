package com.example.eventticketing.infrastructure.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbConfig {
    @Bean
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }
}


