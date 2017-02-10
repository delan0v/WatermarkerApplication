package com.feldman.blazej;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

/**
 * Klasa bazowa konfiguracji kontekstu Springa i konfiguracji bazy danych
 * <p>
 * Dostęp do bazy ustawiany jest oddzielnie w pliku resources/jdbc_test.properties
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WatermarkingAppApplication.class)
@Configuration
@PropertySource("classpath:jdbc_test.properties")
public class BaseSpringDatabseConfig {

    @Value("${db.name}")
    private String name;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.driver}")
    private String driver;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url + name);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
