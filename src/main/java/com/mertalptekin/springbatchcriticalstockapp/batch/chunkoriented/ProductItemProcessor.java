package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;

import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkDto;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ProductItemProcessor implements ItemProcessor<ProductChunkDto, ProductChunkDto> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ProductChunkDto process(ProductChunkDto item) throws Exception {
        // ilgili item verikaynağında mevcut mu kontorlü yapılacak
        // eğer mevcut değilse, item'ın createdAt alanı güncellenecek
        // eğer mevcut ise, item'ın updatedAt alanı güncellenecek
        // item'ın updated alanı true olarak işaretlenecek

        String  query = "SELECT COUNT(*) FROM CRITICAL_STOCK_PRODUCT_CHUNK WHERE ID = ?";

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, item.getId());

        // process üzerinde item'ın id'si ile sorgu yapılıyor ve kayıt varsa ilgili item değerlerini güncelliyoruz

        if(count > 0){
            item.setUpdated(true);
            item.setUpdatedAt(LocalDateTime.now());
        } else {
            item.setCreatedAt(LocalDateTime.now());
        }

        return item;
    }
}
