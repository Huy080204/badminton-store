package com.mgr.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import liquibase.integration.spring.SpringLiquibase;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class TenantLiquibaseConfig {

    @Value("${spring.liquibase.change-log}")
    private String changeLog;

    @Value("${spring.liquibase.contexts:dev}")
    private String contexts;

    @Bean
    @Primary
    public SpringLiquibase liquibase(@Qualifier("defaultDataSource") DataSource dataSource) {
        return createLiquibase(dataSource);
    }


    @Bean
    @DependsOn("dataSource")
    public SpringLiquibase liquibaseTenant1(@Qualifier("tenant1DataSource") DataSource dataSource) {
        return createLiquibase(dataSource);
    }

    @Bean
    @DependsOn("dataSource")
    public SpringLiquibase liquibaseTenant2(@Qualifier("tenant2DataSource") DataSource dataSource) {
        return createLiquibase(dataSource);
    }

    private SpringLiquibase createLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLog);
        liquibase.setContexts(contexts);
        return liquibase;
    }
}
