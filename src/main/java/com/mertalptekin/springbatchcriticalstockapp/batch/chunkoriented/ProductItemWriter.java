package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;

import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkDto;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductItemWriter implements ItemWriter<ProductChunkDto> {
    @Autowired
    private DataSource dataSource;

    @Override
    public void write(Chunk<? extends ProductChunkDto> chunk) throws Exception {
        // bir önceki chunk dosyasından okunan verilere göre chunk bazlı updated olucak yada insert edilcek nesleri bulup ayrı ayrı yazdırmam lazım
        // chunk itemlar immutable çalıştıklarından dolayı unmodifiable list olarak alınıyor
        List<ProductChunkDto> updatedProducts = chunk.getItems().stream().filter(x-> x.isUpdated()).collect(Collectors.toUnmodifiableList());

        List<ProductChunkDto> newProducts = chunk.getItems().stream().filter((x-> !x.isUpdated())).collect(Collectors.toUnmodifiableList());

        // aynı anda 2 update 3 insert işlemi yapılıyor olabilir bu durumda tetk tek değerlendirmek lazım.

        if(!updatedProducts.isEmpty()){
            System.out.println("Updated Size:" + updatedProducts.size());
            JdbcBatchItemWriter<ProductChunkDto> writer = new JdbcBatchItemWriterBuilder<ProductChunkDto>().sql("UPDATE CRITICAL_STOCK_PRODUCT_CHUNK SET stock = ?,updated = ?, updatedAt= ? WHERE id= ?").itemPreparedStatementSetter(new ProductUpdateItemSetter()).dataSource(dataSource).build();
            writer.write(new Chunk<>(updatedProducts));
        }


        if(!newProducts.isEmpty()){
            JdbcBatchItemWriter<ProductChunkDto> writer = new JdbcBatchItemWriterBuilder<ProductChunkDto>().sql("INSERT INTO CRITICAL_STOCK_PRODUCT_CHUNK (id, name, price, stock, createdAt) VALUES (?, ?, ?, ?, ?)").itemPreparedStatementSetter(new ProductCreateItemSetter()).dataSource(dataSource).build();
            System.out.println("New Products Size:" + newProducts.size());
            writer.write(new Chunk<>(newProducts));
        }

    }
}
