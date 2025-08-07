package com.mertalptekin.springbatchcriticalstockapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductChunkRequestDto {
    private List<ProductChunkDto> value;
}
