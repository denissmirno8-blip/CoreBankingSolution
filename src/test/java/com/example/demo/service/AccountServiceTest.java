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
import com.example.demo.entities.Balance;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.services.AccountService;

@SpringBootTest
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    private Long accountId;

    @BeforeEach
    void setUp() {
        AccountRequest request = new AccountRequest();
        request.setCustomerId(123L);
        request.setCurrencies(List.of("USD", "EUR"));
        request.setCountry("Estonia");

        Account account = accountService.insert(request);
        accountId = account.getAccountId();
    }

    @Test
    public void accountCustomerTest() {
        Account account = accountService.findById(accountId);
        assertEquals(123L, account.getCustomerId());
    }

    @Test
    public void balancesTableTest() {
        List<Balance> balances = accountService.findByAccountId(accountId);

        assertEquals(2, balances.size());
        List<String> curreList = balances.stream()
                .map(Balance::getCurrency)
                .toList();

        assertTrue(curreList.containsAll(List.of("USD", "EUR")));
    }

    @Test
    void invalidCurrencyExceptionTest() {
        AccountRequest request = new AccountRequest();
        request.setCustomerId(1L);
        request.setCurrencies(List.of("INVALID"));

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            accountService.insert(request);
        });

        assertEquals("Invalid currency!", exception.getMessage());
    }

    @Test
    void customerIdExceptionTest() {
        AccountRequest request = new AccountRequest();

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            accountService.insert(request);
        });

        assertEquals("Customer id is null!", exception.getMessage());
    }

    @Test
    void notFoundAccountExceptionTest() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.findById(1000L);
        });

        assertEquals("Invalid account!", exception.getMessage());
    }
}
