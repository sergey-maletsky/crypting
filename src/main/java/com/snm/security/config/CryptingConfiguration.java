package com.snm.security.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource({
        "classpath:spring/application.properties",
        "classpath:spring/database.properties"
})
public class CryptingConfiguration {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setOutOfOrder(true);
        flyway.repair();
        flyway.migrate();

        return flyway;
    }
}
