package com.mertalptekin.springbatchcriticalstockapp.batch.creditscore;

import com.mertalptekin.springbatchcriticalstockapp.entity.CreditCustomer;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemWriter implements ItemWriter<CreditCustomer> {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void write(Chunk<? extends CreditCustomer> chunk) throws Exception {
        JpaItemWriter<CreditCustomer> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        jpaItemWriter.write(chunk);

        // Not: Her bir job çalıştırma işleminde yeniden insert işlemi yapılır.

        // CreditCustomerRepository creditCustomerRepository = new CreditCustomerRepository();
    }
}
