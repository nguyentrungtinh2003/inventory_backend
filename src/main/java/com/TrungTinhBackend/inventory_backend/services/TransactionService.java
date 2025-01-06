package com.TrungTinhBackend.inventory_backend.services;

import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.dto.TransactionRequest;
import com.TrungTinhBackend.inventory_backend.enums.TransactionStatus;

public interface TransactionService {
    Response purchase(TransactionRequest transactionRequest);
    Response sell(TransactionRequest transactionRequest);
    Response returnToSupplier(TransactionRequest transactionRequest);
    Response getAllTransaction(int page,int size,String filter);
    Response getAllTransactionById(Long id);
    Response getAllTransactionByMonthAndYear(int month,int year);
    Response updateTransaction(Long id, TransactionStatus transactionStatus);
}
