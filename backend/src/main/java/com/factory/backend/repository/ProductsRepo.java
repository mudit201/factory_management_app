package com.factory.backend.repository;

import com.factory.backend.model.Batch;
import com.factory.backend.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ProductsRepo extends JpaRepository<Products, String> {
//    @Query("SELECT b FROM Batch b JOIN Products p ON b.product = p.product WHERE p.product = :id")
//    @Query("SELECT b FROM Products p LEFT JOIN Batch b ON b.product = p.product WHERE p.product = :id")
    @Query("SELECT b FROM Batch b WHERE b.productsEntity.product = :id")
    List<Batch> getBatchesByProductId(@Param("id") String id);

    @Query("SELECT p.product FROM Products p WHERE p.product=:product")
    String findProductById(@Param("product") String product);
}
