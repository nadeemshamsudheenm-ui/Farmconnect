package com.farmconnect.controller;

import com.farmconnect.dto.PublicProductResponse;
import com.farmconnect.entity.ProductCategory;
import com.farmconnect.service.ProductCatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class ProductCatalogController {

    private final ProductCatalogService catalogService;

    public ProductCatalogController(ProductCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * Public Product Catalog for buyers.
     * STRICT PRIVACY GUARANTEE: Zero farmer personal identifiable information (PII) returned.
     */
    @GetMapping("/products")
    public ResponseEntity<List<PublicProductResponse>> getPublicCatalog(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search) {
        List<PublicProductResponse> catalog = catalogService.getPublicCatalog(category, search);
        return ResponseEntity.ok(catalog);
    }

    /**
     * Public detail view for an individual product listing.
     * Strictly anonymized.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<PublicProductResponse> getProductDetails(@PathVariable Long id) {
        PublicProductResponse product = catalogService.getPublicProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ProductCategory>> getCategories() {
        return ResponseEntity.ok(Arrays.asList(ProductCategory.values()));
    }
}
