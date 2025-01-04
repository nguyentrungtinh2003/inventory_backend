package com.TrungTinhBackend.inventory_backend.repositories;

import com.TrungTinhBackend.inventory_backend.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
