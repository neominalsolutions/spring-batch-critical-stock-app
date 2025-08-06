package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import com.mertalptekin.springbatchcriticalstockapp.dto.Product;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriticalStockDecider implements JobExecutionDecider {

    // Inject Service or Repository if needed
    // Bussiness Service buna göre bir karar mantığıda uygulanabilir.
    // JobExecution veya Step Execution üzerinden gerekli veriler alınabilir. Buna göre bir karar verilebilir.

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

        System.out.println("Critical Stock Decider");

        List<Product> products = (List<Product>) jobExecution.getExecutionContext().get("products");



        if(products.size() == 0){
            System.out.println("No Product found");
            return new FlowExecutionStatus("NO_PRODUCTS_FOUND");
        } else {
            var isExist = products.stream().anyMatch(x-> x.getStock() < 15);

            if(!isExist)
                return new FlowExecutionStatus("NO_CRITICAL_STOCK_FOUND");
            else
                return FlowExecutionStatus.COMPLETED;
        }

    }
}
