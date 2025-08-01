package com.factory.backend.service;

import com.factory.backend.dto.BatchProductDTO;
import com.factory.backend.dto.ProductDto;
import com.factory.backend.exception.DuplicateProductException;
import com.factory.backend.model.Batch;
import com.factory.backend.model.Products;
import com.factory.backend.repository.ProductsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductsRepo repo;

    public List<ProductDto> getAllProducts(){
        List<Products> products = repo.findAll();
        return products.stream()
                .map(this::convertProductToProductDto)
                .collect(Collectors.toList());
    }

    public Boolean findProduct(String product){
        Boolean val = (repo.findProductById(product)!=null);
        System.out.println(val);
        return val;
    }

//    public List<Batch> getBatchesByProductId(String product){return repo.getBatchesByProductId(product);}

//    @Transactional
    public List<BatchProductDTO> getBatchesByProductId(String product){
        List<Batch> batches = repo.getBatchesByProductId(product);
        return batches.stream()
                .map(this::convertToBatchProductDTO)
                .collect(Collectors.toList());
    }

    public BatchProductDTO convertToBatchProductDTO(Batch batch){
        return new BatchProductDTO(
                batch.getBatch(),
                batch.getProduct(),
                batch.getOperator(),
                batch.getStart_time(),
                batch.getEnd_time(),
                batch.getDate(),
                batch.getProductsEntity().getFlavor(),
                batch.getProductsEntity().getSize(),
                batch.getProductsEntity().getMinBatchTime()
        );
    }

    public ProductDto addNewProduct(Products product) {
        Optional<Products> existingProduct = repo.findById(product.getProduct());
        if(existingProduct.isPresent()){
            throw new DuplicateProductException("Product with Id '"+ product.getProduct()+"' already exist");
        }

        Products productEntity = new Products();
        productEntity.setProduct(product.getProduct());
        productEntity.setSize(product.getSize());
        productEntity.setFlavor(product.getFlavor());
        productEntity.setMinBatchTime(product.getMinBatchTime());

        try{
            repo.save(productEntity);
            return convertProductToProductDto(productEntity);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ProductDto convertProductToProductDto(Products product){
        ProductDto dto = new ProductDto();
        dto.setProduct(product.getProduct());
        dto.setProduct(product.getProduct());
        dto.setSize(product.getSize());
        dto.setFlavor(product.getFlavor());
        dto.setMin_batch_time(product.getMinBatchTime());
        return dto;
    }

    public ResponseEntity<?> deleteProductById(String productId) {
        if(!repo.existsById(productId)){
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(productId);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
