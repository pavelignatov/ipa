package com.ipa.database.postgre;

import com.google.common.collect.ImmutableMap;
import com.ipa.database.postgre.dao.PostgreTransactions;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "datasource.postgres")
@EnableJpaRepositories(entityManagerFactoryRef = "postgreEntityManagerFactory",
        transactionManagerRef = PostgreTransactions.JPA)
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PostgreConfig extends DataSourceProperties {

    private @NonNull Boolean embedded;

    @Bean
    PlatformTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager(postgreEntityManagerFactory().getObject());
    }

    @Bean
    LocalContainerEntityManagerFactoryBean postgreEntityManagerFactory() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Map<String, Object> jpaProperties = new HashMap<String, Object>(6) {{
            put("hibernate.show_sql",true);
            put("hibernate.max_fetch_depth",3);
            put("hibernate.jdЬc.fetch_size",50);
            put("hibernate.jdbc.batch_size",10);
            put("hibernate.hbm2ddl.auto","create-drop");
        }};

        if (!embedded) {
            jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
        }

        factoryBean.setJpaPropertyMap(jpaProperties);

        factoryBean.setDataSource(posrgreDatasource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan("com.ipa.database.postgre.dao.entities");

        return factoryBean;
    }

    @Bean
    JdbcTemplate postgreJdbc() {
        return new JdbcTemplate(posrgreDatasource());
    }

    @Bean
    PlatformTransactionManager jdbcTransactionManager() {
        return new DataSourceTransactionManager(posrgreDatasource());
    }

    @Bean
    DataSource posrgreDatasource() {
        if (embedded) {
            log.info("HSQL datasource is set");
            return new EmbeddedDatabaseBuilder().
                    setType(EmbeddedDatabaseType.HSQL).
                    setName("ipa").
                    build();
        }
        log.info("Postgres datasource is set");
        return initializeDataSourceBuilder()
                .build();
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
