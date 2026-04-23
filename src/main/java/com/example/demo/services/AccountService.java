package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.demo.entities.Account;
import com.example.demo.entities.Balance;
import com.example.demo.entities.AccountRequest;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.AccountMapper;
import com.example.demo.mappers.BalanceMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.accounts}")
    private String accountRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    public List<String> allowedCurrencies = List.of("EUR", "SEK", "GBP", "USD");

    // Add not find id error
    public Account findById(Long id) {
        Account account = accountMapper.findById(id);
        if (account == null)
            throw new ResourceNotFoundException("Invalid account!");
        return account;
    }

    @TransactionalEventListener
    public Account insert(AccountRequest request) {
        Account account = new Account();
        if (request.getCustomerId() == null)
            throw new InvalidDataException("Customer id is null!");
        else
            account.setCustomerId(request.getCustomerId());

        List<Balance> allBalances = new ArrayList<>();

        accountMapper.insert(account);
        Long currentAccountId = account.getAccountId();

        for (String currency : request.getCurrencies()) {
            if (!allowedCurrencies.contains(currency))
                throw new InvalidDataException("Invalid currency!");

            Balance newBalance = new Balance();
            newBalance.setAmount(0);
            newBalance.setCurrency(currency);
            newBalance.setAccountId(currentAccountId);
            balanceMapper.insert(newBalance);
            allBalances.add(newBalance);
        }
        account.setBalances(allBalances);
        // send json file to broker
        rabbitTemplate.convertAndSend(exchange, accountRoutingKey, account);
        return account;
    }

    public Long getAccountsLength() {
        return accountMapper.countAccountTableRows();
    }

    public List<Balance> findByAccountId(Long accountId) {
        return balanceMapper.findByAccountId(accountId);
    }

    public List<Long> findAllAccounts() {
        return accountMapper.findAllIds();
    }

}
