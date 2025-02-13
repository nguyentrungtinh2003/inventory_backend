package com.TrungTinhBackend.inventory_backend.services;

import com.TrungTinhBackend.inventory_backend.dto.ProductDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile img) throws IOException;
    Response getAllProduct();
    Response getProductById(Long id);
    Response updateProduct(Long id, ProductDTO productDTO, MultipartFile img) throws IOException;
    Response deleteProduct(Long id);
    Response searchProduct(String input);
}
