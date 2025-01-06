package com.TrungTinhBackend.inventory_backend.controllers;

import com.TrungTinhBackend.inventory_backend.dto.CategoryDTO;
import com.TrungTinhBackend.inventory_backend.dto.ProductDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createProduct(@RequestPart(name = "product") @Valid ProductDTO productDTO,
                                                  @RequestPart(name = "img") MultipartFile img) {
        return ResponseEntity.ok(productService.saveProduct(productDTO,img));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id,
                                                  @RequestPart(name = "product") @Valid ProductDTO productDTO,
                                                  @RequestPart(name = "img") MultipartFile img) {
        return ResponseEntity.ok(productService.updateProduct(id,productDTO,img));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
