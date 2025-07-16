package com.factory.backend.repository;

import com.factory.backend.dto.BatchDto;
import com.factory.backend.dto.ProductivityDto;
import com.factory.backend.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface BatchRepo extends JpaRepository<Batch, Long> {

    @Query("SELECT DISTINCT b.operator FROM Batch b ")
    List<String> findAllOperators();

    @Query("SELECT b FROM Batch b WHERE b.operator=:operator")
    List<Batch> findBatchByOperator(@Param("operator") String operator);

    @Query("SELECT DISTINCT b.operator FROM Batch b WHERE b.operator=:operator")
    String findOperatorByName(@Param("operator") String operator);

//    @Query("SELECT b, b.end_time - b.start_time AS total_time FROM Batch b WHERE b,batch=:batch")
    @Query(value = "SELECT b.batch, b.product, b.operator, TIMEDIFF(b.end_time, b.start_time) AS totalTime, p.min_batch_time " +
            "FROM line_productivity b " +
            "JOIN products p ON b.product = p.product " +
            "WHERE b.batch = :batch", nativeQuery = true)
    ProductivityDto findBatchByBatchId(@Param("batch") Long batch);


}
