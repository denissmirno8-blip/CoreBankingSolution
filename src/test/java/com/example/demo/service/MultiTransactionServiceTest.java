package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Account;
import com.example.demo.entities.AccountRequest;
import com.example.demo.entities.Transaction;
import com.example.demo.services.AccountService;
import com.example.demo.services.TransactionService;

@SpringBootTest
@Transactional
public class MultiTransactionServiceTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    private Long accountId;
    private Transaction testingTransaction;
    private int transactionsCount = 5;

    @BeforeEach
    void setUp() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCustomerId(123L);
        accountRequest.setCurrencies(List.of("USD", "EUR"));
        accountRequest.setCountry("Estonia");

        Account account = accountService.insert(accountRequest);

        Transaction transactionRequest = new Transaction();
        accountId = account.getAccountId();

        // Fori loop with in out transaction by i
        for (int i = 0; i < transactionsCount; i++) {
            transactionRequest.setAccountId(accountId);
            transactionRequest.setAmount(100);
            transactionRequest.setCurrency("EUR");
            transactionRequest.setDirection(i % 2 == 0 ? "IN" : "OUT");
            transactionRequest.setDescription("Test transactions");

            testingTransaction = transactionService.insert(transactionRequest);
        }
    }

    @Test
    public void multiTransactionAmountTest() {
        assertEquals(transactionsCount % 2 == 0 ? 0 : 100, testingTransaction.getAmount());
    }

    @Test
    public void getTransactionTest() {
        List<Transaction> listOfTransactions = transactionService.findTransactionsById(accountId);
        assertEquals(transactionsCount, listOfTransactions.size());
    }
}
