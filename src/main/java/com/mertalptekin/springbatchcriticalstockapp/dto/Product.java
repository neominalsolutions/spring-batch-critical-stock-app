package com.mertalptekin.springbatchcriticalstockapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

// Not: ExecutionContext de veri saklarken Serializable olarak işaretlemeliyiz yada JsonString olarak objectMapper ile jsonString dönüştürebiliriz.
// Not: Serializable tanımlı nesnede çok fazla veri çekmek performans sorunlarına yol açabilir.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @JsonProperty("ProductID")
    private Integer id;

    @JsonProperty("ProductName")
    private String name;

    @JsonProperty("UnitPrice")
    private BigDecimal price;

    @JsonProperty("UnitsInStock")
    private Integer stock;

}
