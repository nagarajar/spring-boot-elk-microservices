package com.elk.product.service.impl;

import com.elk.product.dto.ProductRequest;
import com.elk.product.dto.ProductResponse;
import com.elk.product.entity.Product;
import com.elk.product.exception.ResourceNotFoundException;
import com.elk.product.mapper.MapperUtil;
import com.elk.product.repository.ProductRepository;
import com.elk.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating product: name={}, price={}, stock={}",
                request.getName(), request.getPrice(), request.getStockQuantity());
        Product product = repository.save(MapperUtil.toProductEntity(request));
        log.info("Product created successfully: id={}, name={}", product.getId(), product.getName());
        return MapperUtil.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Update product: name={}, price={}, stock={}",
                request.getName(), request.getPrice(), request.getStockQuantity());
        getProductById(id);
        Product product = getProduct(id);

        product.setProductCode(request.getProductCode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setStatus(request.getStatus());

        Product updated = repository.save(product);
        log.info("Product updated successfully: id={}, name={}", updated.getId(), updated.getName());
        return MapperUtil.toProductResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with id={}", id);
        Product product = getProduct(id);
        repository.delete(product);
        log.warn("Product deleted: id={}, name={}", product.getId(), product.getName());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id={}", id);
        return MapperUtil.toProductResponse(getProduct(id));
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        log.info("Fetching all products");
        return repository.findAll().stream().map(MapperUtil::toProductResponse).toList();
    }

    public Product getProduct(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id = " + id));
    }
}
