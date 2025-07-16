package com.factory.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "downtime_factors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Factors {
    @Id
    @Column(name = "factor", nullable = false)
    private Integer factor;
    @Column(name = "description", length = 100, nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "operator_error", length = 3)
    private OperatorError operatorError;

    public enum OperatorError {
        Yes,
        No
    }

}
