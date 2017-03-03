package com.ipa.database.mysql;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "datasource.mysql")
public class MySqlConfig extends DataSourceProperties {
    @Bean
    public DataSource mysqlDataSource() {
        return initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate jdbcMySql() {
        return new JdbcTemplate(mysqlDataSource());
    }
}