package com.factory.backend.dto;

import com.factory.backend.model.Factors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactorDetailsDto {
    private String description;
    private Factors.OperatorError operatorError;
}
