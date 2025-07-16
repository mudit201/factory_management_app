package com.factory.backend.dto;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDto {
    private Long batch;
    private String product;
    private String operator;
    private LocalTime start_time;
    private LocalTime end_time;
    private LocalDate date;
}
