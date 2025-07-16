package com.factory.backend.service;

import com.factory.backend.dto.FactorDetailsDto;
import com.factory.backend.model.Factors;
import com.factory.backend.model.LineDowntime;
import com.factory.backend.repository.LineDowntimeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class LineDowntimeService {
    @Autowired
    LineDowntimeRepo repo;

    public LineDowntime getLineDowntimeByBatch(BigInteger batch) {
        return repo.findLineDowntimeByBatch(batch);
    }

    public FactorDetailsDto getFactorDetailsByFactor(Integer factor){
        Factors factorEntity = repo.findFactorDetailById(factor);
        return convertFactorsToFactorDetailsDto(factorEntity);
    }

    public FactorDetailsDto convertFactorsToFactorDetailsDto(Factors factors){
        if(factors == null){
            return null;
        }
        return new FactorDetailsDto(
                factors.getDescription(),
                factors.getOperatorError()
        );
    }
}
