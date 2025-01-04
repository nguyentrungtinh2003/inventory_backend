package com.TrungTinhBackend.inventory_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    @Positive(message = "Product id is required")
    private Long productId;

    @Positive(message = "Quantity id is required")
    private Integer quantityId;

    @Positive(message = "Supplier id is required")
    private Long supplierId;

    private String description;

    private String note;

}
