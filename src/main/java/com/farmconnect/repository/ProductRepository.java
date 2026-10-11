package com.farmconnect.repository;

import com.farmconnect.entity.Product;
import com.farmconnect.entity.ProductCategory;
import com.farmconnect.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByFarmerId(Long farmerId);

    List<Product> findByStatusAndStockQuantityGreaterThan(ProductStatus status, BigDecimal minStock);

    List<Product> findByCategoryAndStatusAndStockQuantityGreaterThan(ProductCategory category, ProductStatus status, BigDecimal minStock);

    @Query("SELECT p FROM Product p WHERE p.status = :status AND p.stockQuantity > :minStock " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> searchPublicCatalog(@Param("query") String query,
                                      @Param("status") ProductStatus status,
                                      @Param("minStock") BigDecimal minStock);

    @Query("SELECT p FROM Product p WHERE p.status = :status AND p.stockQuantity > :minStock " +
           "AND p.category = :category " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> searchPublicCatalogWithCategory(@Param("query") String query,
                                                  @Param("category") ProductCategory category,
                                                  @Param("status") ProductStatus status,
                                                  @Param("minStock") BigDecimal minStock);
}
