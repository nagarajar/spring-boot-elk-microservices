package com.elk.product.controller;

import com.elk.product.dto.ApiResponse;
import com.elk.product.dto.ProductRequest;
import com.elk.product.dto.ProductResponse;
import com.elk.product.mapper.MapperUtil;
import com.elk.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse productResponse = service.createProduct(request);
        ApiResponse<ProductResponse> apiResponse = MapperUtil.buildApiResponse(HttpStatus.CREATED, "Product created successfully",
                productResponse, getPath());
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        ProductResponse productResponse = service.updateProduct(id, request);
        ApiResponse<ProductResponse> apiResponse = MapperUtil.buildApiResponse(HttpStatus.OK, "Product updated successfully", productResponse, getPath());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id){
        ProductResponse productResponse = service.getProductById(id);
        ApiResponse<ProductResponse> apiResponse = MapperUtil.buildApiResponse(HttpStatus.OK, "Product fetched successfully", productResponse, getPath());
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> deleteProductById(@PathVariable Long id){
        service.deleteProduct(id);
        ApiResponse<ProductResponse> apiResponse = MapperUtil.buildApiResponse(HttpStatus.OK, "Product deleted successfully", null, getPath());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(){
        List<ProductResponse> productResponses = service.getAllProduct();
        ApiResponse<List<ProductResponse>> apiResponse = MapperUtil.buildApiResponse(HttpStatus.OK, "All product fetched successfully", productResponses, getPath());
        return ResponseEntity.ok(apiResponse);
    }

    private String getPath() {
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }
}
