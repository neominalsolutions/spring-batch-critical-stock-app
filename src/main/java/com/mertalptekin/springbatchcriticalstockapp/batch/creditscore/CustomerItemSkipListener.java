package com.mertalptekin.springbatchcriticalstockapp.batch.creditscore;

import com.mertalptekin.springbatchcriticalstockapp.dto.CustomerDto;
import com.mertalptekin.springbatchcriticalstockapp.entity.CreditCustomer;
import org.springframework.batch.core.SkipListener;

public class CustomerItemSkipListener implements SkipListener<CustomerDto, CreditCustomer> {

    @Override
    public void onSkipInWrite(CreditCustomer item, Throwable t) {
        System.out.println("CustomerItemSkipListener.onSkipInWrite" + item + " " + t.getMessage());
        // Yazma işleminde eğer atlatılan bir durum varsa bunu logla, veritabanına kaydet
        SkipListener.super.onSkipInWrite(item, t);
    }

    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println("CustomerItemSkipListener.onSkipInRead" + t.getMessage());
        SkipListener.super.onSkipInRead(t);
    }

    @Override
    public void onSkipInProcess(CustomerDto item, Throwable t) {
        // Okuma işleminde eğer atlatılan bir durum varsa bunu logla, veritabanına kaydet
        System.out.println("CustomerItemSkipListener.onSkipInProcess" + item + " " + t.getMessage());
        SkipListener.super.onSkipInProcess(item, t);
    }
}
