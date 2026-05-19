package com.keycap.keycapdesign;

import com.keycap.keycapdesign.dto.product.ProductRequest;
import com.keycap.keycapdesign.dto.product.ProductResponse;
import com.keycap.keycapdesign.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;

@SpringBootTest
class KeycapDesignBeApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    void contextLoads() {
    }

    @Test
    void createAndListProducts() {
        ProductRequest request = new ProductRequest();
        request.setName("Test Keycap");
        request.setPrice(BigDecimal.valueOf(19.99));
        request.setTheme(ProductTheme.MINIMAL);
        request.setLayoutType(LayoutType.TKL);
        request.setKeyProfile(KeyProfile.OEM);
        ProductResponse created = productService.createProduct(request);

        List<ProductResponse> products = productService.listProducts(null, null, null, null, null);
        assert created.getId() != null;
        assert products.stream().anyMatch(p -> p.getId().equals(created.getId()));
    }

}
