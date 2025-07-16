package com.factory.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    @Id
    @Column(name="product", length = 10, nullable = false)
    private String product;
    @Column(name="flavor", length = 50, nullable = false)
    private String flavor;
    @Column(name="size", length = 20, nullable = false)
    private String size;
    @Column(name="min_batch_time", nullable = false)
    private int minBatchTime;

    @OneToMany(mappedBy = "productsEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Batch> batches = new ArrayList<>();
}
