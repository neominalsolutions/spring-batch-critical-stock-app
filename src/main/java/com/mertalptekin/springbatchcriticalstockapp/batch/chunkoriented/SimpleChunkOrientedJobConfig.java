package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;


import com.mertalptekin.springbatchcriticalstockapp.dto.Product;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class SimpleChunkOrientedJobConfig {

    @Bean
    public ItemReader<Product> itemReader() {
        System.out.println("ItemReader bean oluşturuldu.");
        // 6 adet okunacak ürün oluşturduk
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product A", BigDecimal.valueOf(10.5),10));
        products.add(new Product(2, "Product B", BigDecimal.valueOf(45.10),20));
        products.add(new Product(3, "Product C", BigDecimal.valueOf(38.50),15));
        products.add(new Product(4, "Product D", BigDecimal.valueOf(200),45));
        products.add(new Product(5, "Product E", BigDecimal.valueOf(20.2),35));
        products.add(new Product(6, "Product F", BigDecimal.valueOf(10.5),350));
        return new ListItemReader<>(products);
    }
    @Bean
    public ItemProcessor<Product, Product> itemProcessor() {
        System.out.println("ItemProcessor bean oluşturuldu.");
        // stok değerlerini 10 arttıralım
        // fiyatlarına yüzde 10 zam yapalım
        return item -> {
            // veri seti üzerinde işlem yapıyoruz.
             item.setStock(item.getStock() + 10);
             item.setPrice((item.getPrice().multiply(new BigDecimal(1.10))));

             return  item;
        };
    }
    @Bean
    public ItemWriter<Product> itemWriter() {
        System.out.println("ItemWriter bean oluşturuldu.");
        return items ->  {

            System.out.println("Items written: " + items.size());

            };

    }

    @Bean
    public Step simpleChunkOrientedStep(@Autowired  JobRepository  jobRepository, @Autowired DataSourceTransactionManager transactionManager) {
        System.out.println("SimpleChunkOrientedStep bean oluşturuldu.");
        // bu senaryo için 3 adet chunk oluşturduk.
      return  new StepBuilder("simpleChunkOrientedStep",jobRepository).<Product,Product>chunk(2,transactionManager).reader(itemReader()).processor(itemProcessor()).writer(itemWriter())
              .listener(
              new ItemProcessListener<Product, Product>() {

                  @Override
                  public void beforeProcess(Product item) {
                      System.out.println("Product Eski Hali: " + item);
                      ItemProcessListener.super.beforeProcess(item);
                  }

                  @Override
                  public void afterProcess(Product item, Product result) {
                      System.out.println("Product Yeni Hali: " + item);
                      ItemProcessListener.super.afterProcess(item, result);
                  }

                  @Override
                  public void onProcessError(Product item, Exception e) {
                      System.out.println("Product Hata durumunu yakalamak: " + item);
                      ItemProcessListener.super.onProcessError(item, e);
                  }
              }
      )
              .listener(new StepExecutionListener() {
          @Override
          public void beforeStep(StepExecution stepExecution) {
              System.out.println("StepExecution beforeStep: " + stepExecution);
              StepExecutionListener.super.beforeStep(stepExecution);
          }

          @Override
          public ExitStatus afterStep(StepExecution stepExecution) {
              System.out.println("StepExecution afterStep: " + stepExecution);
              return StepExecutionListener.super.afterStep(stepExecution);
          }
      }).build();
    }

    @Bean(name="simpleChunkOrientedJob")
    public Job simpleChunkOrientedJob(@Autowired  JobRepository  jobRepository, @Autowired DataSourceTransactionManager transactionManager) {
        return new JobBuilder("simpleChunkOrientedJob", jobRepository)
                .start(simpleChunkOrientedStep(jobRepository, transactionManager)).listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {

                        System.out.println("Job başlamadan önce çalışacak.");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        System.out.println("Job tamamlandıktan sonra çalışacak.");
                        if (jobExecution.getStatus().isUnsuccessful()) {
                            System.out.println("Job başarısız oldu.");
                        } else {
                            System.out.println("Job başarılı bir şekilde tamamlandı.");
                        }
                    }
                })
                .build();
    }


}
