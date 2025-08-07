package com.mertalptekin.springbatchcriticalstockapp.batch.creditscore;

import com.mertalptekin.springbatchcriticalstockapp.dto.CustomerDto;
import com.mertalptekin.springbatchcriticalstockapp.entity.CreditCustomer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemProcessor implements ItemProcessor<CustomerDto, CreditCustomer> {
    @Override
    public CreditCustomer process(CustomerDto item) throws Exception {

        // buradaki processor bu durumda filter ve entity map amaçlı kullanılıdı.
        if(item.getCreditScore() < 600){

            var creditCustomer = new CreditCustomer();
            creditCustomer.setAge(item.getAge());
            creditCustomer.setName(item.getName());
            creditCustomer.setCreditScore(item.getCreditScore());

            return creditCustomer;
        }

        // 600 den küçük ise kredi notu kullanmayacağız
        // Not: null olan değerler writer gönderilmez. writer sadece koşula uyanları batchwrite işlemine sokar.
        return null;
    }
}
