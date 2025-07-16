package com.factory.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Table(name = "line_downtime")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineDowntime {
    @Id
    @Column(name = "batch", nullable = false)
    private BigInteger batch;

    private Integer f1;
    private Integer f2;
    private Integer f3;
    private Integer f4;
    private Integer f5;
    private Integer f6;
    private Integer f7;
    private Integer f8;
    private Integer f9;
    private Integer f10;
    private Integer f11;
    private Integer f12;
}
