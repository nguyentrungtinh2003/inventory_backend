package com.TrungTinhBackend.inventory_backend.services;

import com.TrungTinhBackend.inventory_backend.dto.CategoryDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDTO categoryDTO);
    Response getAllCategory();
    Response getCategoryById(Long id);
    Response updateCategory(Long id,CategoryDTO categoryDTO);
    Response deleteCategory(Long id);
}
