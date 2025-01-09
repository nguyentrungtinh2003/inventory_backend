package com.TrungTinhBackend.inventory_backend.services.impl;

import com.TrungTinhBackend.inventory_backend.dto.CategoryDTO;
import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.dto.SupplierDTO;
import com.TrungTinhBackend.inventory_backend.exceptions.NotFoundException;
import com.TrungTinhBackend.inventory_backend.models.Category;
import com.TrungTinhBackend.inventory_backend.models.Supplier;
import com.TrungTinhBackend.inventory_backend.repositories.SupplierRepository;
import com.TrungTinhBackend.inventory_backend.services.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = modelMapper.map(supplierDTO,Supplier.class);
        supplierRepository.save(supplier);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Create supplier success!");
        response.setData(supplier);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));

        List<SupplierDTO> supplierDTOS = modelMapper.map(suppliers,new TypeToken<SupplierDTO>() {}.getType());

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get all supplier success!");
        response.setData(supplierDTOS);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier not found"));
        SupplierDTO supplierDTO = modelMapper.map(supplier,SupplierDTO.class);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get supplier by id success!");
        response.setData(supplierDTO);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier not found"));

        if(supplierDTO.getName() != null){
            supplier.setName(supplierDTO.getName());
        }
        if(supplierDTO.getContactInfo() != null){
            supplier.setContactInfo(supplierDTO.getContactInfo());
        }
        if(supplierDTO.getAddress() != null){
            supplier.setAddress(supplierDTO.getAddress());
        }
        supplierRepository.save(supplier);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Update supplier success!");
        response.setData(supplier);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new NotFoundException("Supplier not found"));
        supplierRepository.deleteById(id);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Delete supplier success!");
        response.setData(supplier);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

}
