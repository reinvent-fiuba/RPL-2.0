package com.example.rpl.RPL.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.hikari.connection-init-sql}")
    private String connectionInitSql;
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private int connectionTimeout;

    @Bean
    @Primary
    @Profile({"prod", "stage", "test"})
    public DataSource dataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create()
            .username(username)
            .password(password)
            .type(HikariDataSource.class)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .url(url)
            .build();
        setTypeSpecificProperties(dataSource);
        return dataSource;
    }

    @Bean
    @FlywayDataSource
    @Profile({"development"})
    DataSource flywayDataSourceTest() {
        return DataSourceBuilder.create()
            .username(username)
            .password(password)
            .url(url)
            .driverClassName("org.h2.Driver")
            .build();
    }

    private void setTypeSpecificProperties(HikariDataSource dataSource) {
        dataSource.setConnectionInitSql(connectionInitSql);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setConnectionTimeout(connectionTimeout);
    }
}
