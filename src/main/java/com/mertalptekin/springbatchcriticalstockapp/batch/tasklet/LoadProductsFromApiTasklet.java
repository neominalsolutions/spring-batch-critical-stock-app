package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import com.mertalptekin.springbatchcriticalstockapp.dto.Product;
import com.mertalptekin.springbatchcriticalstockapp.dto.ProductRequestDto;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class LoadProductsFromApiTasklet implements Tasklet {

    @Autowired
    private RestTemplate restTemplate;



    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String apiUrl = "https://services.odata.org/northwind/northwind.svc/Products?$format=json"; // API endpoint

       var response =  restTemplate.exchange(apiUrl,HttpMethod.GET,null,ProductRequestDto.class);
        List<Product> products = response.getBody().getValue();

        System.out.println("LoadProductsFromApiTasklet");

        if(products == null || products.isEmpty()) {
            System.out.println("No products found in the API response.");
            // ilgili tasklet içerisinde sürecin çıkması için exit status kullanırız.
            contribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED; // İşlem tamamlandı, ancak FAILED olarak işaretlendi
        } else {
            // manuel süreç var okuma başarılı ise işaretleyelim
            contribution.incrementReadCount();
            contribution.incrementWriteCount(products.size()); // İşleme alınacak sayı

            chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("products", products);

            // product fail test
            // chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("products", List.of());
            return RepeatStatus.FINISHED;
        }

    }
}
