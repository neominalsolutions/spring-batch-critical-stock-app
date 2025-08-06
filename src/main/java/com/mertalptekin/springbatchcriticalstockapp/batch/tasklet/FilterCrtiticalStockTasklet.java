package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import com.mertalptekin.springbatchcriticalstockapp.dto.Product;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterCrtiticalStockTasklet implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<Product> products = (List<Product>) chunkContext.getStepContext().getStepExecution().getJobExecution()
                .getExecutionContext().get("products");

        if (products == null || products.isEmpty()) {
            System.out.println("No products found in the context.");
            contribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED; // İşlem tamamlandı, ancak FAILED olarak işaretlendi
        } else {
          List<Product> filteredProducts =  products.stream().filter(x-> x.getStock() <15).toList();

          // Eğer filtrelenmiş kaç adet ürün var bunu veri kaynağı kaydetmek için yaptık.
          contribution.incrementFilterCount(filteredProducts.size());

          chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("filteredProducts", List.of(filteredProducts));
        }

        return null;
    }
}
