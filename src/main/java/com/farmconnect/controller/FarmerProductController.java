package com.farmconnect.controller;

import com.farmconnect.dto.FarmerProductResponse;
import com.farmconnect.dto.ProductCreateUpdateRequest;
import com.farmconnect.service.ProductCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmer/products")
public class FarmerProductController {

    private final ProductCatalogService catalogService;

    public FarmerProductController(ProductCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<FarmerProductResponse>> getFarmerProducts(
            @RequestParam(required = false, defaultValue = "0") Long farmerId) {
        List<FarmerProductResponse> products = catalogService.getFarmerProducts(farmerId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FarmerProductResponse>> getAllFarmerProducts() {
        List<FarmerProductResponse> products = catalogService.getFarmerProducts(0L);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<FarmerProductResponse> createProduct(@Valid @RequestBody ProductCreateUpdateRequest request) {
        FarmerProductResponse response = catalogService.createProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmerProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductCreateUpdateRequest request) {
        FarmerProductResponse response = catalogService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") Long farmerId) {
        catalogService.deleteProduct(id, farmerId);
        return ResponseEntity.noContent().build();
    }
}
