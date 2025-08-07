package com.mertalptekin.springbatchcriticalstockapp.batch.creditscore;

import com.mertalptekin.springbatchcriticalstockapp.dto.CustomerDto;
import com.mertalptekin.springbatchcriticalstockapp.entity.CreditCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@EnableBatchProcessing
@Configuration
public class CustomerJobConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager entityTransactionManager;


    @Autowired
    private CustomerItemProcessor customerItemProcessor;

    @Autowired
    private CustomerItemWriter customerItemWriter;


    @Bean
    public  FlatFileItemReader<CustomerDto> customerItemReader() {
        FlatFileItemReader<CustomerDto> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("customer.csv")); // Kaynak dosyayı ayarla (örneğin, bir dosya yolu)
        reader.setLinesToSkip(1); // Başlık satırını atla
        var lineMapper = new DefaultLineMapper<CustomerDto>();
        var delimiter = new DelimitedLineTokenizer();
        lineMapper.setLineTokenizer(delimiter);
        // csv customerDto nesnesine dönüştürme işlemi için FieldSetMapper kullanılır
        lineMapper.setFieldSetMapper(fieldSet -> {
            CustomerDto customer = new CustomerDto();
            customer.setName(fieldSet.readString("name"));
            customer.setAge(fieldSet.readInt("age"));
            customer.setCreditScore(fieldSet.readInt("creditScore"));
            return customer;
        });
        // Delimiter ayarları ve hangi alanların okunacağını belirtme
        delimiter.setNames(new String[] {  "name", "age", "creditScore" });
        delimiter.setDelimiter(",");
        reader.setLineMapper(lineMapper);

        return  reader;
    }



    @Bean
    public Step customerCsvStep() {
        return new StepBuilder("customerCsvStep", jobRepository)
                .<CustomerDto,CreditCustomer>chunk(10, entityTransactionManager)
                .reader(customerItemReader())
                .processor(customerItemProcessor)
                .writer(customerItemWriter)
                .build();
    }

    @Bean(name = "customerFlowJob")
    public Job customerFlowJob() {
        return new JobBuilder("customerFlowJob", jobRepository)
                .start(customerCsvStep())
                .build();
    }


}
