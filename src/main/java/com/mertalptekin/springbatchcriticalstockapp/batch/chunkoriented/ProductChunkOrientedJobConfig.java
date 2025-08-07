package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;

import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@EnableBatchProcessing
@Configuration
public class ProductChunkOrientedJobConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private ProductItemReader productItemReader;

    @Autowired
    private ProductItemProcessor productItemProcessor;

    @Autowired
    private ProductItemWriter productItemWriter;

    @Bean
    public Step chunkOrientedProductStep(){
        return new StepBuilder("chunkOrientedProductStep",jobRepository).<ProductChunkDto,ProductChunkDto>chunk(4,transactionManager).reader(productItemReader).processor(productItemProcessor).writer(productItemWriter).build();
    }

    @Bean("chunkOrientedProductJob")
    public Job chunkOrientedProductJob() {
        return new JobBuilder("chunkOrientedProductJob", jobRepository)
                .start(chunkOrientedProductStep())
                .build();
    }


}
