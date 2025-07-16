package com.factory.backend.service;

import com.factory.backend.dto.BatchDto;
import com.factory.backend.dto.BatchProductDTO;
import com.factory.backend.dto.OperatorDto;
import com.factory.backend.dto.ProductivityDto;
import com.factory.backend.model.Batch;
import com.factory.backend.repository.BatchRepo;
import com.factory.backend.repository.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchService {
    @Autowired
    BatchRepo repo;

    @Autowired
    ProductService productService;

    @Autowired
    ProductsRepo productsRepo;

//    public List<Batch> getAllBatches(){return repo.findAll();}
    public List<BatchDto> getAllBatches(){
        List<Batch> batches = repo.findAll();

        return batches.stream()
                .map(this::convertBatchToBatchDto)
                .collect(Collectors.toList());
    }

    public BatchDto getBatchById(Long batch) {
        Batch batchEntity = repo.findById(batch).orElse(null);

        if(batchEntity == null){
            return null;
        }
        return convertBatchToBatchDto(batchEntity);
    }

    public List<BatchDto> getBatchByOperator(String operator){
        List<Batch> batches= repo.findBatchByOperator(operator);

        return batches.stream()
                .map(this::convertBatchToBatchDto)
                .collect(Collectors.toList());
    }

    public BatchDto convertBatchToBatchDto(Batch batch){
        return new BatchDto(
                batch.getBatch(),
                batch.getProduct(),
                batch.getOperator(),
                batch.getStart_time(),
                batch.getEnd_time(),
                batch.getDate()
        );
    }

    public List<OperatorDto> getAllOperator(){
        List<String> operators = repo.findAllOperators();

        return operators.stream()
                .map(this::convertToOperatorDto)
                .collect(Collectors.toList());
    }

    public OperatorDto convertToOperatorDto(String operator){
        return new OperatorDto(operator);
    }

    //to patch the batch
    public BatchDto saveBatch(BatchDto newBatch){
        Batch batchDto= convertBatchDtoToBatch(newBatch);
        Batch savedBatch = repo.save(batchDto);
        return convertBatchToBatchDto(savedBatch);
    }

    public Batch convertBatchDtoToBatch(BatchDto dto) {
        Batch batch = new Batch();
        batch.setBatch(dto.getBatch());
        batch.setOperator(dto.getOperator());
        batch.setStart_time(dto.getStart_time());
        batch.setEnd_time(dto.getEnd_time());
        batch.setDate(dto.getDate());
        batch.setProduct(dto.getProduct());
        return batch;
    }


    public void deleteBatchById(Long id) {
        repo.deleteById(id);
    }

    public Boolean findOperator(String operator){
        Boolean val = (repo.findOperatorByName(operator)!=null);
        System.out.println(val);
        return val;
    }

    //for productivity
    //Since you're doing a native query that computes TIMEDIFF, use a DTO to map the result instead of trying to map it into the Batch entity.
    public ProductivityDto findProductivity(Long batch){
        ProductivityDto batchEntity = repo.findBatchByBatchId(batch);
        return new ProductivityDto(
                batchEntity.getBatch(),
                batchEntity.getProduct(),
                batchEntity.getOperator(),
                batchEntity.getTotal_time(),
                batchEntity.getMin_batch_time()
        );
    }

    public String addBatchForExistingProduct(Batch batchEntity){
        String productId = batchEntity.getProduct();

        if(productService.findProduct(productId) == null){
            return "Product does not exist";
        }else {
            if(batchEntity.getDate() == null){
                batchEntity.setDate(LocalDate.now());
            }
            try{
                repo.save(batchEntity);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return "created";
        }


    }


}
