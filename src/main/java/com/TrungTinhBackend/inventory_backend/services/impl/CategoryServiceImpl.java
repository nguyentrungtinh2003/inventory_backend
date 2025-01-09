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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        categoryRepository.save(category);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Create category success!");
        response.setData(category);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getAllCategory() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        categories.forEach(category -> category.setProducts(null));
        List<CategoryDTO> categoryDTOS = modelMapper.map(categories,new TypeToken<CategoryDTO>() {}.getType());

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get all category success!");
        response.setData(categoryDTOS);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get category by id success!");
        response.setData(categoryDTO);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Update category success!");
        response.setData(category);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        categoryRepository.deleteById(id);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Delete category success!");
        response.setData(category);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
