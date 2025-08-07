package com.mertalptekin.springbatchcriticalstockapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductChunkDto {

    @JsonProperty("ProductID")
    private Integer id;

    @JsonProperty("ProductName")
    private String name;

    @JsonProperty("UnitPrice")
    private BigDecimal price;

    @JsonProperty("UnitsInStock")
    private Integer stock;

    @JsonIgnore
    private boolean updated;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    private LocalDateTime createdAt;

}
