package com.factory.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "line_productivity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch", updatable = false, nullable = false)
    private Long batch;
    @Column(name = "product", length = 10, nullable = false)
    private String product;
    @Column(name = "operator", length = 50, nullable = false)
    private String operator;
    @Column(name = "start_time", nullable = false)
    private LocalTime start_time;
    @Column(name = "end_time", nullable = false)
    private LocalTime end_time;
    @Column(name = "date", nullable = false)
    private LocalDate date;

//    insertable = false, updatable = false on the @JoinColumn in Batch
//    tells Hibernate not to manage the foreign key from the productsEntity
//    side because product is a separate field managed directly.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product", referencedColumnName = "product", insertable = false, updatable = false)
    private Products productsEntity;

}
