package com.factory.backend.controller;

import com.factory.backend.dto.BatchProductDTO;
import com.factory.backend.dto.ProductDto;
import com.factory.backend.exception.DuplicateProductException;
import com.factory.backend.model.Batch;
import com.factory.backend.model.Products;
import com.factory.backend.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductsController {
    @Autowired
    ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }


    @GetMapping("/products/{product}/batches")
    public ResponseEntity<List<BatchProductDTO>> getBatchByProductId(@PathVariable String product){
        if(service.findProduct(product)){
            return new ResponseEntity<>(service.getBatchesByProductId(product), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/products")
    public ResponseEntity<?> addNewProduct(@RequestBody Products product){
        try {
            ProductDto createdProduct = service.addNewProduct(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (DuplicateProductException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add product: " + e.getMessage());
        }
    }

    //how to do it not straight
    // as need to remove entries from batch table first
    //first thought of deleting all the records of the bath
    // with the given product id manually
    //not so optimistic approach ig
    //other approaches:

    @Transactional
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable String productId){
        return service.deleteProductById(productId);
    }

}
