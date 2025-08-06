package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
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

    @Autowired
    private CriticalStockDecider criticalStockDecider;


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

    // Not: Yukarıdaki sürecin akışsız hali
    // 2.örnek Flow kullanımı
    @Bean
    public Flow loadProductsFromApiFlow() {
        return new FlowBuilder<SimpleFlow>("loadProductsFromApiFlow")
                .start(loadProductsFromApiStep())
                .build();
    }

    @Bean
    public Flow proccessCriticalStockFlow() {
        return new FlowBuilder<SimpleFlow>("processCriticalStockFlow")
                .start(criticalStockJobStep())
                .next(saveCriticalStockStep())
                .build();
    }

    @Bean(name = "criticalStockJobFlow")
    public Job criticalStockFlowJob() {
        return new JobBuilder("criticalStockJobFlow", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(loadProductsFromApiFlow())
                .next(criticalStockDecider)
                .on("NO_PRODUCTS_FOUND").fail()
                .on("NO_CRITICAL_STOCK_FOUND").fail()
                .on("COMPLETED").to(proccessCriticalStockFlow())
                .build()
                .build(); // JobBuilder
    }

    // to: akışlar arası geçişi sağlar
    // from: akışın başlangıç noktasını belirtir to: bu akışa geç
    // on: akışın durumunu belirtir. Örneğin, "COMPLETED" durumu başarılı bir şekilde tamamlandığını gösterir.

}
