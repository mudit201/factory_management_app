package com.factory.backend.controller;

import com.factory.backend.dto.FactorDetailsDto;
import com.factory.backend.model.LineDowntime;
import com.factory.backend.service.LineDowntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/api")
public class LineDowntimeController {
    @Autowired
    LineDowntimeService service;

    @GetMapping("/line-downtime/{batch}")
    public ResponseEntity<LineDowntime> getLineDowntimeByBatch(@PathVariable BigInteger batch){
        LineDowntime lineDowntime= service.getLineDowntimeByBatch(batch);
        if(lineDowntime != null){
            return new ResponseEntity<>(service.getLineDowntimeByBatch(batch),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //api for getting the description of the factor
    @GetMapping("/line-downtime/{factor}/description")
    public ResponseEntity<FactorDetailsDto> getFactorDetailsByFactor(@PathVariable Integer factor){
        FactorDetailsDto factorEntity = service.getFactorDetailsByFactor(factor);
        if(factorEntity!=null){
            return new ResponseEntity<>(factorEntity, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
