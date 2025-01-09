package com.TrungTinhBackend.inventory_backend.services.impl;

import com.TrungTinhBackend.inventory_backend.dto.ProductDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.exceptions.NotFoundException;
import com.TrungTinhBackend.inventory_backend.models.Category;
import com.TrungTinhBackend.inventory_backend.models.Product;
import com.TrungTinhBackend.inventory_backend.repositories.CategoryRepository;
import com.TrungTinhBackend.inventory_backend.repositories.ProductRepository;
import com.TrungTinhBackend.inventory_backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    private static final String IMAGE_DIR = System.getProperty("user.dir") + "/product-img/";

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile img) {

        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new NotFoundException("Category not found"));

        Product product = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if (img != null && !img.isEmpty()) {
            log.info("Img file exist");
            String imgPath = saveImg(img);
            product.setImg(imgPath);
        }

        productRepository.save(product);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Create product success!");
        response.setData(product);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    private String saveImg(MultipartFile img) {
        if(img.getContentType().startsWith("image/") || img.getSize() > 1024 * 1024 *1024) {
            throw new IllegalArgumentException("Only img file under");
        }

        File directory = new File(IMAGE_DIR);
        if(!directory.exists()) {
            directory.mkdir();
            log.info("Directory was created");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + img.getOriginalFilename();

        String imgPath = IMAGE_DIR + uniqueFileName;

        try{
            File destinationFile = new File(imgPath);
            img.transferTo(destinationFile);
        }catch(Exception e) {
            throw new IllegalArgumentException("Error saving img");
        }

        return imgPath;
    }

    @Override
    public Response getAllProduct() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<ProductDTO> productDTOS = modelMapper.map(products,new TypeToken<List<ProductDTO>>() {}.getType());

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get all product success!");
        response.setData(productDTOS);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }


    @Override
    public Response getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found "));

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get product by id success!");
        response.setData(product);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response updateProduct(Long id, ProductDTO productDTO, MultipartFile img) {
        Product product = productRepository.findById(productDTO.getProductId()).orElseThrow(() -> new NotFoundException("Product not found "));

if(img != null && !img.isEmpty()) {
    String imgPath = saveImg(img);
    product.setImg(imgPath);
}
if(productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0) {
    Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new NotFoundException("Category not found "));

    product.setCategory(category);
}
if(productDTO.getName() != null && !productDTO.getName().isBlank()) {
    product.setName(productDTO.getName());
}
        if(productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            product.setSku(productDTO.getSku());
        }
        if(productDTO.getDescription() != null && !productDTO.getDescription().isEmpty()) {
            product.setDescription(productDTO.getDescription());
        }
        if(productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            product.setPrice(productDTO.getPrice());
        }
        if(productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            product.setStockQuantity(productDTO.getStockQuantity());
        }
        productRepository.save(product);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Update product success!");
        response.setData(product);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found "));
        productRepository.deleteById(id);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Delete product success!");
        response.setData(product);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response searchProduct(String input) {
        List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input,input);
        if(products.isEmpty()) {
            throw new NotFoundException("Products not found");
        }
        List<ProductDTO> productDTOS = modelMapper.map(products,new TypeToken<List<ProductDTO>>() {}.getType());

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Search product success!");
        response.setData(productDTOS);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
