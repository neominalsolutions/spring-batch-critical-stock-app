package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@EnableBatchProcessing
@Configuration
public class CriticalStockJobConfig {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private LoadProductsFromApiTasklet loadProductsFromApiTasklet;
    @Autowired
    private FilterCriticalStockTasklet filterCriticalStockTasklet;
    @Autowired
    private SaveCriticalStockTasklet saveCriticalStockTasklet;
    @Bean
    public  Step loadProductsFromApiStep() {
        return new StepBuilder("loadProductsFromApiStep", jobRepository)
                .tasklet(loadProductsFromApiTasklet, transactionManager)
                .build();
    }
    @Bean
    public Step criticalStockJobStep() {
        return new StepBuilder("criticalStockJobStep", jobRepository)
                .tasklet(filterCriticalStockTasklet, transactionManager)
                .build();
    }
    @Bean
    public Step saveCriticalStockStep() {
        return new StepBuilder("saveCriticalStockStep", jobRepository)
                .tasklet(saveCriticalStockTasklet, transactionManager)
                .build();
    }
    @Bean(name = "criticalStockJob")
    public Job criticalStockJob() {
        return new JobBuilder("criticalStockJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(loadProductsFromApiStep())
                .next(criticalStockJobStep())
                .next(saveCriticalStockStep())
                .build();
    }

}
