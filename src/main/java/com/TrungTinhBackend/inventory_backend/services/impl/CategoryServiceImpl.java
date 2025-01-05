package com.TrungTinhBackend.inventory_backend.services.impl;

import com.TrungTinhBackend.inventory_backend.dto.CategoryDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.exceptions.NotFoundException;
import com.TrungTinhBackend.inventory_backend.models.Category;
import com.TrungTinhBackend.inventory_backend.repositories.CategoryRepository;
import com.TrungTinhBackend.inventory_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        categoryRepository.save(category);

        return Response.builder()
                .status(200)
                .message("Create category success !")
                .data(category)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response getAllCategory() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        categories.forEach(category -> category.setProducts(null));
        List<CategoryDTO> categoryDTOS = modelMapper.map(categories,new TypeToken<CategoryDTO>() {}.getType());

        return Response.builder()
                .status(200)
                .message("Get all category success !")
                .data(categoryDTOS)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);

        return Response.builder()
                .status(200)
                .message("Get category by id success !")
                .data(categoryDTO)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);

        return Response.builder()
                .status(200)
                .message("Update category success !")
                .data(category)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Delete category success !")
                .data(category)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
