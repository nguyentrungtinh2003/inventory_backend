package com.TrungTinhBackend.inventory_backend.services.impl;

import com.TrungTinhBackend.inventory_backend.dto.Response;
import com.TrungTinhBackend.inventory_backend.dto.TransactionDTO;
import com.TrungTinhBackend.inventory_backend.dto.TransactionRequest;
import com.TrungTinhBackend.inventory_backend.enums.TransactionStatus;
import com.TrungTinhBackend.inventory_backend.enums.TransactionType;
import com.TrungTinhBackend.inventory_backend.exceptions.NameValueRequiredException;
import com.TrungTinhBackend.inventory_backend.exceptions.NotFoundException;
import com.TrungTinhBackend.inventory_backend.models.Product;
import com.TrungTinhBackend.inventory_backend.models.Supplier;
import com.TrungTinhBackend.inventory_backend.models.Transaction;
import com.TrungTinhBackend.inventory_backend.models.User;
import com.TrungTinhBackend.inventory_backend.repositories.ProductRepository;
import com.TrungTinhBackend.inventory_backend.repositories.SupplierRepository;
import com.TrungTinhBackend.inventory_backend.repositories.TransactionRepository;
import com.TrungTinhBackend.inventory_backend.services.TransactionService;
import com.TrungTinhBackend.inventory_backend.services.UserService;
import com.TrungTinhBackend.inventory_backend.specification.TransactionFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Response purchase(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if(supplierId == null) {
            throw new NameValueRequiredException("Supplier id is required");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product not found ")
        );
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new NotFoundException("Supplier not found ")
        );
        User user = userService.getCurrentLoggedInUser();
        product.setStockQuantity(product.getStockQuantity() + quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .transactionStatus(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .note(transactionRequest.getNote())
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Save purchase success!");
        response.setData(transaction);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product not found ")
        );
        User user = userService.getCurrentLoggedInUser();
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .transactionStatus(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .note(transactionRequest.getNote())
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Save sale success!");
        response.setData(transaction);
        response.setTimestamp(LocalDateTime.now());

        return response;

    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if(supplierId == null) {
            throw new NameValueRequiredException("Supplier id is required");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product not found ")
        );
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new NotFoundException("Supplier not found ")
        );
        User user = userService.getCurrentLoggedInUser();
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .transactionStatus(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .note(transactionRequest.getNote())
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Return to supplier success!");
        response.setData(transaction);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getAllTransaction(int page, int size, String filter) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"id"));

        Specification<Transaction> spec = TransactionFilter.byFilter(filter);
        Page<Transaction> transactionPage = transactionRepository.findAll(spec,pageable);

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactionPage.getContent(),new TypeToken<List
                <List<TransactionDTO>>>() {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
            transactionDTO.setProduct(null);
        });

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get transaction by page success!");
        response.setData(transactionDTOS);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getAllTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()-> new NotFoundException("Transaction not found"));

        TransactionDTO transactionDTO = modelMapper.map(transaction,TransactionDTO.class);
        transactionDTO.setUser(null);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get transaction by id success!");
        response.setData(transactionDTO);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {
        List<Transaction> transactions = transactionRepository.findAll(TransactionFilter.byMonthAndYear(month,year));

        List<TransactionDTO> transactionDTOS = modelMapper.map(transactions,new TypeToken<List<TransactionDTO>>() {}.getType());

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setUser(null);
            transactionDTO.setSupplier(null);
            transactionDTO.setProduct(null);
        });

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Get transaction by month, year success!");
        response.setData(transactionDTOS);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    @Override
    public Response updateTransaction(Long id, TransactionStatus transactionStatus) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()-> new NotFoundException("Transaction not found"));

        transaction.setTransactionStatus(transactionStatus);
        transaction.setUpdateAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        Response response = new Response();
        response.setStatus(200);
        response.setMessage("Update transaction success!");
        response.setData(transaction);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
