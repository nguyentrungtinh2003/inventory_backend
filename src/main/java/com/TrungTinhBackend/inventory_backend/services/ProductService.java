package com.TrungTinhBackend.inventory_backend.services;

import com.TrungTinhBackend.inventory_backend.dto.ProductDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile img);
    Response getAllProduct();
    Response getProductById(Long id);
    Response updateProduct(Long id, ProductDTO productDTO, MultipartFile img);
    Response deleteProduct(Long id);
    Response searchProduct(String input);
}
