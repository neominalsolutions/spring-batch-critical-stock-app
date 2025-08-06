package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@EnableBatchProcessing
@Configuration
public class SimpleTaskletJobConfig {

    // Not: Bu kısımda Tasklet Job örneğimiz ile ilgili tanımlamaları yapıcaz.
    // Not: Her tasklet için bir step tanımlanır ve bu step'ler job içerisinde kullanılır.

    // Job Logicler burada yer alır.
    @Bean
    public Tasklet springBatchTasklet() {
        return (contribution, chunkContext) -> {
            // Tasklet içeriği burada tanımlanır.
            System.out.println("Spring Batch Tasklet çalıştı.");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean(name = "sampleTaskletStep")
    public Step taskletStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("taskletStep", jobRepository)
                .tasklet(springBatchTasklet(),transactionManager)
                .build();
    }

    // Jobda Stepleri çağırır.
    @Bean(name = "sampleTaskletJob")
    public Job taskletJob(JobRepository jobRepository,DataSourceTransactionManager transactionManager) {
        return new JobBuilder("taskletJob",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(taskletStep(jobRepository,transactionManager))
                .build();
    }





}
