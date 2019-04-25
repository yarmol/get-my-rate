package me.jarad.rates.config;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import lombok.extern.slf4j.Slf4j;
import me.jarad.rates.handler.ApplicationStartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

/**
 * Embedded DB settings
 * Created by Vitalii Yarmolenko (yarmol@gmail.com) on 23.04.19.
 */
@Configuration
@Slf4j
public class EmbeddedDbConfig {

    private static final String DB_SERVICE = "dbServiceBean";

    @Bean(name = {DB_SERVICE})
    public MariaDB4jSpringService mariaDB4jSpringService() {
        return new MariaDB4jSpringService();
    }

    @Bean
    @DependsOn(DB_SERVICE)
    public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService,
                                 @Value("${app.mariaDB4j.databaseName}") String databaseName,
                                 @Value("${spring.datasource.username}") String datasourceUsername,
                                 @Value("${spring.datasource.password}") String datasourcePassword,
                                 @Value("${spring.datasource.driver-class-name}") String datasourceDriver) {

        try {
            mariaDB4jSpringService.getDB().createDB(databaseName);

            DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

            return DataSourceBuilder
                    .create()
                    .username(datasourceUsername)
                    .password(datasourcePassword)
                    .url(config.getURL(databaseName))
                    .driverClassName(datasourceDriver)
                    .build();
        } catch (ManagedProcessException e) {
            log.error("Database initialization error, {}", e.getMessage());
            throw new ApplicationStartException("Database initialization error", e);
        }
    }
}
