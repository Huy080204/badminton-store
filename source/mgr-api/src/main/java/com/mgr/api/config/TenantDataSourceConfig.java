package com.mgr.api.config;

import com.mgr.api.multitenancy.TenantRoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TenantDataSourceConfig {

    @Value("${spring.datasource.url}")
    private String defaultUrl;
    @Value("${spring.datasource.username}")
    private String defaultUsername;
    @Value("${spring.datasource.password}")
    private String defaultPassword;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${tenant.datasource.tenant1.url}")
    private String tenant1Url;
    @Value("${tenant.datasource.tenant1.username}")
    private String tenant1Username;
    @Value("${tenant.datasource.tenant1.password}")
    private String tenant1Password;

    @Value("${tenant.datasource.tenant2.url}")
    private String tenant2Url;
    @Value("${tenant.datasource.tenant2.username}")
    private String tenant2Username;
    @Value("${tenant.datasource.tenant2.password}")
    private String tenant2Password;

    @Bean(name = "defaultDataSource")
    @Lazy
    public DataSource defaultDataSource() {
        return createDataSource(defaultUrl, defaultUsername, defaultPassword);
    }

    @Bean(name = "tenant1DataSource")
    @Lazy
    public DataSource tenant1DataSource() {
        return createDataSource(tenant1Url, tenant1Username, tenant1Password);
    }

    @Bean(name = "tenant2DataSource")
    @Lazy
    public DataSource tenant2DataSource() {
        return createDataSource(tenant2Url, tenant2Username, tenant2Password);
    }

    @Bean
    @Primary
    @Lazy
    public DataSource dataSource(
            @Lazy @Qualifier("defaultDataSource") DataSource defaultDataSource,
            @Lazy @Qualifier("tenant1DataSource") DataSource tenant1DataSource,
            @Lazy @Qualifier("tenant2DataSource") DataSource tenant2DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        
        targetDataSources.put("default", defaultDataSource);
        targetDataSources.put("db_tenant1", tenant1DataSource);
        targetDataSources.put("db_tenant2", tenant2DataSource);
        
        // Also keep short names for compatibility
        targetDataSources.put("tenant1", tenant1DataSource);
        targetDataSources.put("tenant2", tenant2DataSource);

        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);

        return routingDataSource;
    }

    private DataSource createDataSource(String url, String username, String password) {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
