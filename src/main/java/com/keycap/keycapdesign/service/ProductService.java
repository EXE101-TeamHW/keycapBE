package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.product.ProductRequest;
import com.keycap.keycapdesign.dto.product.ProductResponse;
import com.keycap.keycapdesign.entity.Product;
import com.keycap.keycapdesign.enums.KeyProfile;
import com.keycap.keycapdesign.enums.LayoutType;
import com.keycap.keycapdesign.enums.ProductTheme;
import com.keycap.keycapdesign.enums.ProductStatus;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.ProductRepository;
import com.keycap.keycapdesign.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> listProducts(ProductTheme theme, LayoutType layoutType, KeyProfile keyProfile,
                                              BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAll().stream()
                .filter(p -> theme == null || theme == p.getTheme())
                .filter(p -> layoutType == null || layoutType == p.getLayoutType())
                .filter(p -> keyProfile == null || keyProfile == p.getKeyProfile())
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        productRepository.save(product);
        return toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        applyRequest(product, request);
        productRepository.save(product);
        return toResponse(product);
    }

    public List<ProductResponse> listAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getImages() != null) {
            product.setImagesJson(JsonUtil.toJson(request.getImages()));
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        } else if (product.getStatus() == null) {
            product.setStatus(ProductStatus.ACTIVE);
        }
        product.setTheme(request.getTheme());
        product.setLayoutType(request.getLayoutType());
        product.setKeyProfile(request.getKeyProfile());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getStockQuantity(), JsonUtil.fromJson(product.getImagesJson()),
                product.getStatus(), product.getCreatedAt(), product.getTheme(),
                product.getLayoutType(), product.getKeyProfile());
    }
}
