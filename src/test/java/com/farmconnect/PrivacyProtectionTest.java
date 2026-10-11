package com.farmconnect;

import com.farmconnect.dto.PublicProductResponse;
import com.farmconnect.entity.FarmerProfile;
import com.farmconnect.entity.Product;
import com.farmconnect.entity.ProductCategory;
import com.farmconnect.entity.Role;
import com.farmconnect.entity.User;
import com.farmconnect.service.ProductCatalogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PrivacyProtectionTest {

    @Autowired
    private ProductCatalogService catalogService;

    @Test
    @DisplayName("STRICT PRIVACY LAYER: Public Catalog MUST NOT leak farmer personal details (PII)")
    void testPublicCatalogStrictPrivacyEnforcement() {
        List<PublicProductResponse> publicProducts = catalogService.getPublicCatalog(null, null);

        assertThat(publicProducts).isNotEmpty();

        for (PublicProductResponse product : publicProducts) {
            // Verify public product attributes exist
            assertThat(product.getId()).isNotNull();
            assertThat(product.getName()).isNotBlank();
            assertThat(product.getPricePerUnit()).isPositive();
            assertThat(product.getStockQuantity()).isNotNull();

            // Verify anonymized region code is provided instead of address
            assertThat(product.getAnonymizedRegion())
                    .isNotBlank()
                    .contains("Zone-");

            // CRITICAL PRIVACY ASSERTION:
            // Ensure no raw private address strings appear in description or region fields
            assertThat(product.getAnonymizedRegion()).doesNotContain("Survey 42/B");
            assertThat(product.getAnonymizedRegion()).doesNotContain("Plot 18");
            assertThat(product.getAnonymizedRegion()).doesNotContain("Village Barnala");

            // Ensure trust badge is rendered
            assertThat(product.getTrustBadge()).isNotBlank();
        }
    }

    @Test
    @DisplayName("STRICT PRIVACY LAYER: Product entity conversion drops user legal name, phone, and address")
    void testDirectConversionGuaranteesZeroPii() {
        User secretFarmerUser = new User(
                "confidential.farmer@example.com",
                "secretPass",
                "Secret Johnathan Doe",
                "+91 99999 88888",
                Role.FARMER
        );
        FarmerProfile secretProfile = new FarmerProfile(
                secretFarmerUser,
                "Doe Highlands",
                "Zone-Alpine-99",
                "Classified Physical Farm Address 123",
                10,
                true,
                "Confidential Bio"
        );
        Product product = new Product(
                secretProfile,
                "Fresh Organic Strawberries",
                ProductCategory.FRUITS,
                "Delicious berry punnets",
                "punnet",
                new BigDecimal("80.00"),
                new BigDecimal("100.00"),
                LocalDate.now(),
                true,
                "https://images.unsplash.com/strawberries"
        );
        product.setId(999L);

        PublicProductResponse publicDto = catalogService.convertToPublicDto(product);

        // Assert public DTO only has product metrics and anonymized region
        assertThat(publicDto.getName()).isEqualTo("Fresh Organic Strawberries");
        assertThat(publicDto.getAnonymizedRegion()).isEqualTo("Zone-Alpine-99");

        // Assert PII is NEVER present in the public DTO representation
        assertThat(publicDto.toString()).doesNotContain("Secret Johnathan Doe");
        assertThat(publicDto.toString()).doesNotContain("+91 99999 88888");
        assertThat(publicDto.toString()).doesNotContain("Classified Physical Farm Address 123");
        assertThat(publicDto.toString()).doesNotContain("confidential.farmer@example.com");
    }
}
