package com.factory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductivityDto {
    private Long batch;
    private String product;
    private String operator;
    private Time total_time;
    private Integer min_batch_time;
}
