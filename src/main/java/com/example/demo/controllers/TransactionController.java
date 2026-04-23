package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Transaction;
import com.example.demo.services.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(path = "/{account_id}")
    public ResponseEntity<List<Transaction>> findByAccountId(@PathVariable("account_id") Long accountId) {
        return ResponseEntity.ok(transactionService.findTransactionsById(accountId));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Transaction transaction) {
        transactionService.insert(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction is created and sended to RabbitMQ.");
    }
}
