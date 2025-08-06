package com.mertalptekin.springbatchcriticalstockapp.batch.tasklet;

import com.mertalptekin.springbatchcriticalstockapp.dto.Product;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.naming.Name;
import java.util.List;

@Component
public class SaveCriticalStockTasklet implements Tasklet {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<Product> criticalStockProducts = (List<Product>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("filteredProducts");
        for (Product criticalStockProduct : criticalStockProducts) {
            String checkSql = "SELECT COUNT(*) FROM CRITICAL_STOCK_PRODUCTS WHERE ID = ?";
            int count = jdbcTemplate.queryForObject(checkSql,
                    new Object[]{criticalStockProduct.getId()},
                    Integer.class);

            if (count > 0) {
              String updateSql = "UPDATE CRITICAL_STOCK_PRODUCTS SET Stock = :Stock WHERE ID = :ID";

                namedParameterJdbcTemplate.update(updateSql, new MapSqlParameterSource()
                        .addValue("ID", criticalStockProduct.getId())
                        .addValue("Stock", criticalStockProduct.getStock()));
                contribution.incrementWriteCount(1);

                System.out.println("Critical stock product updated: " + criticalStockProduct.getName());

            } else {
                String insertSql = "INSERT INTO CRITICAL_STOCK_PRODUCTS (ID, Name, Price, Stock) VALUES (:ID, :Name,:Price, :Stock)";
                namedParameterJdbcTemplate.update(insertSql, new MapSqlParameterSource()
                        .addValue("ID", criticalStockProduct.getId())
                        .addValue("Name", criticalStockProduct.getName())
                        .addValue("Price", criticalStockProduct.getPrice())
                        .addValue("Stock", criticalStockProduct.getStock()));
                contribution.incrementWriteCount(1);
                System.out.println("Critical stock product inserted: " + criticalStockProduct.getName());
            }
        }

        // Not: Eğer tasklet içerisinde bir runtime exception fırlatılmazsa, step başarısız olarak kabul edilir. Yani failed olarak işaretlenir.
        return RepeatStatus.FINISHED;
    }
}
