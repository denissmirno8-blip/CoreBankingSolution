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
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.AccountService;
import com.example.demo.services.TransactionService;

@SpringBootTest
@Transactional
public class TransationServiceTest {
    // test transaction insert
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    private Long accountId;
    private Transaction testingTransaction;

    @BeforeEach
    void setUp() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setCustomerId(123L);
        accountRequest.setCurrencies(List.of("USD", "EUR"));
        accountRequest.setCountry("Estonia");

        Account account = accountService.insert(accountRequest);

        Transaction transactionRequest = new Transaction();
        accountId = account.getAccountId();

        transactionRequest.setAccountId(accountId);
        transactionRequest.setAmount(100);
        transactionRequest.setCurrency("EUR");
        transactionRequest.setDirection("IN");
        transactionRequest.setDescription("Test transactions");

        testingTransaction = transactionService.insert(transactionRequest);
    }

    // Test transaction account id, amount, currency, direction,
    // description, balance after
    @Test
    public void transactionAccountIdTest() {
        assertEquals(accountId, testingTransaction.getAccountId());
    }

    @Test
    public void transactionAmountTest() {
        assertEquals(100, testingTransaction.getAmount());
    }

    @Test
    public void transactionCurrencyTest() {
        assertEquals("EUR", testingTransaction.getCurrency());
    }

    @Test
    public void transactionDirectionTest() {
        assertEquals("IN", testingTransaction.getDirection());
    }

    @Test
    public void transactionDescriptionTest() {
        assertEquals("Test transactions", testingTransaction.getDescription());
    }

    @Test
    public void transactionBalanceAfterTest() {
        assertEquals(100, testingTransaction.getBalanceAmountAfterTransaction());
    }

    // Test Invalid data

    @Test
    void invalidAccountExceptionTest() {
        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(100000L);
        transactionRequest.setAmount(100);
        transactionRequest.setCurrency("EUR");
        transactionRequest.setDirection("IN");
        transactionRequest.setDescription("Test transactions");

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.insert(transactionRequest);
        });

        assertEquals("Account not found!", exception.getMessage());
    }

    @Test
    void invalidCurrencyExceptionTest() {
        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(accountId);
        transactionRequest.setAmount(100);
        transactionRequest.setCurrency("SEK");
        transactionRequest.setDirection("IN");
        transactionRequest.setDescription("Test transactions");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            transactionService.insert(transactionRequest);
        });

        assertEquals("Invalid currency!", exception.getMessage());
    }

    @Test
    void invalidDirectionExceptionTest() {
        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(accountId);
        transactionRequest.setAmount(100);
        transactionRequest.setCurrency("EUR");
        transactionRequest.setDirection("INVALID");
        transactionRequest.setDescription("Test transactions");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            transactionService.insert(transactionRequest);
        });

        assertEquals("Invalid direction!", exception.getMessage());
    }

    @Test
    void invalidAmountExceptionTest() {
        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(accountId);
        transactionRequest.setAmount(-100);
        transactionRequest.setCurrency("EUR");
        transactionRequest.setDirection("IN");
        transactionRequest.setDescription("Test transactions");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            transactionService.insert(transactionRequest);
        });

        assertEquals("Invalid amount! Amount is negative.", exception.getMessage());
    }

    @Test
    void insufficientFundExceptionTest() {
        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(accountId);
        transactionRequest.setAmount(999999999);
        transactionRequest.setCurrency("EUR");
        transactionRequest.setDirection("OUT");
        transactionRequest.setDescription("Test transactions");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            transactionService.insert(transactionRequest);
        });

        assertEquals("Insufficient funds on the balance!", exception.getMessage());
    }

    @Test
    void invalidDescriptionTest() {
        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(accountId);
        transactionRequest.setAmount(100);
        transactionRequest.setCurrency("EUR");
        transactionRequest.setDirection("IN");
        transactionRequest.setDescription("");

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            transactionService.insert(transactionRequest);
        });

        assertEquals("Description missing!", exception.getMessage());
    }

    // multitransactions test + get transactions lists test

}
