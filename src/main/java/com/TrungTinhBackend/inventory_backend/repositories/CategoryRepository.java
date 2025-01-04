package com.TrungTinhBackend.inventory_backend.repositories;

import com.TrungTinhBackend.inventory_backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
