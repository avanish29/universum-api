package com.universum.service.i18service.config;

import com.universum.service.i18service.migration.DBMigrationCallback;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {
    @Bean
    public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
        return configuration -> configuration.callbacks(new DBMigrationCallback());
    }
}
