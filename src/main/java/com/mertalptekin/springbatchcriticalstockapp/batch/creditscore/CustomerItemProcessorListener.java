package com.mertalptekin.springbatchcriticalstockapp.batch.creditscore;

import com.mertalptekin.springbatchcriticalstockapp.dto.CustomerDto;
import com.mertalptekin.springbatchcriticalstockapp.entity.CreditCustomer;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

// Not: Itemreader,ItemProcessor,ItemWriter gibi süreçlerde Step ve Job Execution durumlarına erişemeyiz. Bu sebep ile context üzerinden veri saklayamayız.


public class CustomerItemProcessorListener implements ItemProcessListener<CustomerDto, CreditCustomer>, StepExecutionListener
{
    private StepExecution stepExecution;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return StepExecutionListener.super.afterStep(stepExecution);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        this.stepExecution.getJobExecution().getExecutionContext().put("lowCreditCustomersCount", 0);
        this.stepExecution.getJobExecution().getExecutionContext().put("highCreditCustomersCount", 0);
        StepExecutionListener.super.beforeStep(stepExecution);
    }

    // Kredi kullanımına uygun olan müşteriler için işlem yapıldıktan sonra bu metot tetiklenir.
    @Override
    public void afterProcess(CustomerDto item, CreditCustomer result) {

        if(result != null){
            Integer highCount = (Integer) this.stepExecution.getJobExecution().getExecutionContext().get("highCreditCustomersCount");
            highCount++;
            this.stepExecution.getJobExecution().getExecutionContext().put("highCreditCustomersCount", highCount);
            System.out.println("Kredi kullanımına uygun olan güncel müşteri sayısı: " + highCount);
        } else {
            Integer lowCount = (Integer) this.stepExecution.getJobExecution().getExecutionContext().get("lowCreditCustomersCount");
            lowCount++;
            this.stepExecution.getJobExecution().getExecutionContext().put("lowCreditCustomersCount", lowCount);
            System.out.println("Kredi kullanımına uygun olmayan müşteri sayısı: " + lowCount);
        }

        ItemProcessListener.super.afterProcess(item, result);
    }
}
