package com.TrungTinhBackend.inventory_backend.controllers;

import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.dto.SupplierDTO;
import com.TrungTinhBackend.inventory_backend.dto.TransactionRequest;
import com.TrungTinhBackend.inventory_backend.enums.TransactionStatus;
import com.TrungTinhBackend.inventory_backend.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity<Response> createPurchase(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.purchase(transactionRequest));
    }

    @PostMapping("/sell")
    public ResponseEntity<Response> sell(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @PostMapping("/return")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransaction(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) String searchFilter) {
        return ResponseEntity.ok(transactionService.getAllTransaction(page, size, searchFilter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getAllTransactionById(id));
    }

    @GetMapping("/by-month-and-year")
    public ResponseEntity<Response> getTransactionByMonthAndYear(@RequestParam int month,
                                                                 @RequestParam int year) {
        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month, year));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateTransaction(@PathVariable Long id, @RequestBody TransactionStatus transactionStatus) {
        return ResponseEntity.ok(transactionService.updateTransaction(id,transactionStatus));
    }
}
