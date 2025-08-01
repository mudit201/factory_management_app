package com.factory.backend.controller;

import com.factory.backend.dto.AddBatchDto;
import com.factory.backend.dto.BatchDto;
import com.factory.backend.dto.OperatorDto;
import com.factory.backend.dto.ProductivityDto;
import com.factory.backend.model.Batch;
import com.factory.backend.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class BatchController {
    @Autowired
    BatchService service;

    @GetMapping("/batches")
    public ResponseEntity<List<BatchDto>> getAllBatches(){
        return new ResponseEntity<>(service.getAllBatches(), HttpStatus.OK);
    }

    @GetMapping("/batches/{batch}")
    public ResponseEntity<BatchDto> getBatchById(@PathVariable Long batch){
        BatchDto batch1 = service.getBatchById(batch);
        return (batch1 != null) ?
                new ResponseEntity<>(batch1, HttpStatus.OK)
                :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PatchMapping("/batches/{batch}")
    public ResponseEntity<?> updateBatchById(@PathVariable Long batch, @RequestBody Batch partialBatch){
        Optional<BatchDto> existingBatchOpt = Optional.ofNullable(service.getBatchById(batch));

        if(existingBatchOpt.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BatchDto existingBatch = existingBatchOpt.get();

        if (partialBatch.getProduct() != null) {
            existingBatch.setProduct(partialBatch.getProduct());
        }
        if (partialBatch.getOperator() != null) {
            existingBatch.setOperator(partialBatch.getOperator());
        }
        if (partialBatch.getStart_time() != null) {
            existingBatch.setStart_time(partialBatch.getStart_time());
        }
        if (partialBatch.getEnd_time() != null) {
            existingBatch.setEnd_time(partialBatch.getEnd_time());
        }
        if (partialBatch.getDate() != null) {
            existingBatch.setDate(partialBatch.getDate());
        }

        return new ResponseEntity<>(service.saveBatch(existingBatch), HttpStatus.OK);

    }

//    @PostMapping("/batches")
//    public ResponseEntity<String> addNewBatch(Batch batchEntity){
//        String res = service.addBatchForExistingProduct(batchEntity);
//        if (Objects.equals(res, "Product does not exist")){
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @PostMapping("/batches")
    public ResponseEntity<String> addNewBatch(@RequestBody AddBatchDto batchDto) {
        Batch batchEntity = new Batch();

        batchEntity.setProduct(batchDto.getProduct());
        batchEntity.setOperator(batchDto.getOperator());
        batchEntity.setStart_time(batchDto.getStart_time());
        batchEntity.setEnd_time(batchDto.getEnd_time());

        // If date is null, set it to today
        if (batchDto.getDate() == null) {
            batchEntity.setDate(LocalDate.now());
        } else {
            batchEntity.setDate(batchDto.getDate());
        }

        try {
            String res = service.addBatchForExistingProduct(batchEntity);
            if ("Product does not exist".equals(res)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Added new batch",HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            // This exception usually wraps SQLIntegrityConstraintViolationException
            return new ResponseEntity<>("Product does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/batch/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        BatchDto batch1= service.getBatchById(id);
        if(batch1 != null){
            service.deleteBatchById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    //Operators api------

    @GetMapping("/operators")
    public ResponseEntity<List<OperatorDto>> getAllOperators(){
        return new ResponseEntity<>(service.getAllOperator(), HttpStatus.OK);
    }

    @GetMapping("/operators/{operatorName}/batches")
    public ResponseEntity<List<BatchDto>> getBatchByOperator(@PathVariable String operatorName){
        if(service.findOperator(operatorName)){
            return new ResponseEntity<>(service.getBatchByOperator(operatorName), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //get data for productivity matrices
    @GetMapping("/productivity/{batch}")
    public ResponseEntity<ProductivityDto> getProductivity(@PathVariable Long batch){
        BatchDto batchEntity = service.getBatchById(batch);
        if (batchEntity!= null){
            return new ResponseEntity<>(service.findProductivity(batch), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
