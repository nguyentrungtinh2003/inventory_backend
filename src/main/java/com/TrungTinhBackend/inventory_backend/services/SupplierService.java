package com.TrungTinhBackend.inventory_backend.services;

import com.TrungTinhBackend.inventory_backend.dto.CategoryDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.dto.SupplierDTO;

public interface SupplierService {
    Response createSupplier(SupplierDTO supplierDTO);
    Response getAllSupplier();
    Response getSupplierById(Long id);
    Response updateSupplier(Long id,SupplierDTO supplierDTO);
    Response deleteSupplier(Long id);
}
