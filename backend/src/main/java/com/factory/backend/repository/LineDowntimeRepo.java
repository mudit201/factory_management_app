package com.factory.backend.repository;

import com.factory.backend.dto.FactorDetailsDto;
import com.factory.backend.model.Factors;
import com.factory.backend.model.LineDowntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface LineDowntimeRepo extends JpaRepository<LineDowntime, BigInteger> {
    @Query("SELECT l from LineDowntime l WHERE l.batch=:batch")
    LineDowntime findLineDowntimeByBatch(@Param("batch") BigInteger batch);

    @Query("SELECT f FROM Factors f WHERE f.factor=:id")
    Factors findFactorDetailById(@Param("id") Integer factor);
}
