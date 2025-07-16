package com.factory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String product;
    private String flavor;
    private String size;
    private Integer min_batch_time;
}
