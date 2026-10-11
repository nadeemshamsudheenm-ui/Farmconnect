package com.farmconnect.service;

import com.farmconnect.dto.FarmerProductResponse;
import com.farmconnect.dto.ProductCreateUpdateRequest;
import com.farmconnect.dto.PublicProductResponse;
import com.farmconnect.entity.FarmerProfile;
import com.farmconnect.entity.Product;
import com.farmconnect.entity.ProductCategory;
import com.farmconnect.entity.ProductStatus;
import com.farmconnect.exception.InvalidOperationException;
import com.farmconnect.exception.ResourceNotFoundException;
import com.farmconnect.repository.FarmerProfileRepository;
import com.farmconnect.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCatalogService {

    private final ProductRepository productRepository;
    private final FarmerProfileRepository farmerProfileRepository;

    public ProductCatalogService(ProductRepository productRepository, FarmerProfileRepository farmerProfileRepository) {
        this.productRepository = productRepository;
        this.farmerProfileRepository = farmerProfileRepository;
    }

    /**
     * Strict Privacy Conversion:
     * Generates a buyer-facing DTO with zero farmer PII.
     * The farmer's full name, phone number, and physical address are NEVER exposed.
     */
    public PublicProductResponse convertToPublicDto(Product product) {
        String anonymizedRegion = "Verified Regional Hub";
        if (product.getFarmer() != null && product.getFarmer().getAnonymizedRegionCode() != null) {
            anonymizedRegion = product.getFarmer().getAnonymizedRegionCode();
        }

        return new PublicProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getUnit(),
                product.getPricePerUnit(),
                product.getStockQuantity(),
                product.getHarvestDate(),
                product.getIsOrganicCertified(),
                product.getImageUrl(),
                product.getStatus(),
                anonymizedRegion,
                product.getIsOrganicCertified() ? "Certified 100% Organic Direct Producer" : "Verified Direct Producer"
        );
    }

    public FarmerProductResponse convertToFarmerDto(Product product) {
        return new FarmerProductResponse(
                product.getId(),
                product.getFarmer().getId(),
                product.getFarmer().getFarmName(),
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getUnit(),
                product.getPricePerUnit(),
                product.getStockQuantity(),
                product.getHarvestDate(),
                product.getIsOrganicCertified(),
                product.getImageUrl(),
                product.getStatus(),
                product.getCreatedAt()
        );
    }

    /**
     * Public Buyer Catalog: lists all active produce with available stock > 0.
     * Guaranteed zero farmer PII.
     */
    @Transactional(readOnly = true)
    public List<PublicProductResponse> getPublicCatalog(ProductCategory category, String search) {
        List<Product> products;
        BigDecimal minStock = BigDecimal.ZERO;

        if (search != null && !search.trim().isEmpty()) {
            String trimmedSearch = search.trim();
            if (category != null) {
                products = productRepository.searchPublicCatalogWithCategory(trimmedSearch, category, ProductStatus.AVAILABLE, minStock);
            } else {
                products = productRepository.searchPublicCatalog(trimmedSearch, ProductStatus.AVAILABLE, minStock);
            }
        } else if (category != null) {
            products = productRepository.findByCategoryAndStatusAndStockQuantityGreaterThan(category, ProductStatus.AVAILABLE, minStock);
        } else {
            products = productRepository.findByStatusAndStockQuantityGreaterThan(ProductStatus.AVAILABLE, minStock);
        }

        return products.stream()
                .map(this::convertToPublicDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PublicProductResponse getPublicProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product listing not found with ID: " + id));

        return convertToPublicDto(product);
    }

    /**
     * Authenticated Farmer Portal: view own product listings
     */
    @Transactional(readOnly = true)
    public List<FarmerProductResponse> getFarmerProducts(Long farmerId) {
        if (farmerId == null || farmerId == 0L) {
            return productRepository.findAll().stream()
                    .map(this::convertToFarmerDto)
                    .collect(Collectors.toList());
        }

        if (!farmerProfileRepository.existsById(farmerId)) {
            throw new ResourceNotFoundException("Farmer profile not found with ID: " + farmerId);
        }

        return productRepository.findByFarmerId(farmerId).stream()
                .map(this::convertToFarmerDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public FarmerProductResponse createProduct(ProductCreateUpdateRequest req) {
        FarmerProfile farmer = farmerProfileRepository.findById(req.getFarmerId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found with ID: " + req.getFarmerId()));

        Product product = new Product(
                farmer,
                req.getName(),
                req.getCategory(),
                req.getDescription(),
                req.getUnit(),
                req.getPricePerUnit(),
                req.getStockQuantity(),
                req.getHarvestDate(),
                req.getIsOrganicCertified(),
                req.getImageUrl()
        );

        if (product.getStockQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        Product saved = productRepository.save(product);
        return convertToFarmerDto(saved);
    }

    @Transactional
    public FarmerProductResponse updateProduct(Long productId, ProductCreateUpdateRequest req) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!product.getFarmer().getId().equals(req.getFarmerId())) {
            throw new InvalidOperationException("Unauthorized: You can only modify your own product listings.");
        }

        product.setName(req.getName());
        product.setCategory(req.getCategory());
        product.setDescription(req.getDescription());
        product.setUnit(req.getUnit());
        product.setPricePerUnit(req.getPricePerUnit());
        product.setStockQuantity(req.getStockQuantity());
        product.setHarvestDate(req.getHarvestDate());
        product.setIsOrganicCertified(req.getIsOrganicCertified());
        if (req.getImageUrl() != null && !req.getImageUrl().trim().isEmpty()) {
            product.setImageUrl(req.getImageUrl());
        }

        if (product.getStockQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else {
            product.setStatus(ProductStatus.AVAILABLE);
        }

        Product updated = productRepository.save(product);
        return convertToFarmerDto(updated);
    }

    @Transactional
    public void deleteProduct(Long productId, Long farmerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (farmerId != null && farmerId > 0 && !product.getFarmer().getId().equals(farmerId)) {
            throw new InvalidOperationException("Unauthorized: You can only delete your own product listings.");
        }

        productRepository.delete(product);
    }
}
