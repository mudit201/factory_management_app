package com.factory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchProductDTO {
    private Long batch;
    private String product;
    private String operator;
    private LocalTime start_time;
    private LocalTime end_time;
    private LocalDate date;
    private String flavor;
    private String size;
    private Integer min_batch_time;
}
