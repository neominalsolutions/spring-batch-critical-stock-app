package com.mertalptekin.springbatchcriticalstockapp;

// Uygulama için gerekli olan Batch Processing konfigürasyonlarını içeren sınıf.




import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

// Bu anatasyon sayesinde Spring Batch uygulamaları için gerekli olan yapılandırmalar otomatik olarak yapılır.
@EnableBatchProcessing
@Configuration
public class BatchConfig {

    // Not: JobRepository, TransactionManager, JdbcTemplate gibi bean'ler
    // Not: Tasklet yapılarında chunk oriented steplerde olduğu gibi built-in reader ve writer gibi yapılar yok bu sebeple ya repository üzerinden ilgili veritabanı işlemleri yapılır ya da JdbcTemplate gibi araçlar kullanılır.
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder
                .dataSource(dataSource).
                packages("com.mertalptekin.springbatchcriticalstockapp.entity").persistenceUnit("appEntityManager").build();
    }

    // Bu bean, JPA işlemleri için gerekli olan EntityManagerFactory'yi oluşturur.
    @Bean
    public PlatformTransactionManager entityTransactionManager(EntityManagerFactory entityManagerFactory) {
        return  new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JobRepository jobRepository(DataSource dataSource, DataSourceTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setDatabaseType("H2");  // In-memory veritabanı için H2
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.setTablePrefix("BATCH_");
        factory.afterPropertiesSet();
        return  factory.getObject();
    }

    @Bean
    public  DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}
