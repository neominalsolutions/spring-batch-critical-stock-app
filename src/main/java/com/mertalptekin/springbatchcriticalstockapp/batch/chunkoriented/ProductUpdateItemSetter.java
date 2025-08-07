package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;

import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkDto;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductUpdateItemSetter implements ItemPreparedStatementSetter<ProductChunkDto> {
    @Override
    public void setValues(ProductChunkDto item, PreparedStatement ps) throws SQLException {
        ps.setInt(1,item.getStock());
        ps.setBoolean(2, item.isUpdated());
        ps.setTimestamp(3, java.sql.Timestamp.valueOf(item.getUpdatedAt()));
        ps.setInt(4, item.getId());
    }
}
