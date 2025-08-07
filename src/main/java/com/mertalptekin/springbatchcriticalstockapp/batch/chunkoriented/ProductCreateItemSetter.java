package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;


import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkDto;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductCreateItemSetter implements ItemPreparedStatementSetter<ProductChunkDto> {
    @Override
    public void setValues(ProductChunkDto item, PreparedStatement ps) throws SQLException {
        ps.setInt(1, item.getId());
        ps.setString(2, item.getName());
        ps.setBigDecimal(3, item.getPrice());
        ps.setInt(4, item.getStock());
        ps.setTimestamp(5,  java.sql.Timestamp.valueOf(item.getCreatedAt()));
    }
}
