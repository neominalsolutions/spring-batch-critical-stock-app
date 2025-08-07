package com.mertalptekin.springbatchcriticalstockapp.dto;

// CSV Dosyasından okunan veriler DTO olarak tanımlanır.
// Customer.csv dosyasındaki veriler CustomerDto olarak mapleriz.

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String name;
    private Integer age;
    private Integer creditScore;
}
