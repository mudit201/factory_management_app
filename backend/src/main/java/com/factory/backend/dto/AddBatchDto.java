package com.factory.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBatchDto {
    private String product;
    private String operator;
    private LocalTime start_time;
    private LocalTime end_time;
    private LocalDate date;
}
