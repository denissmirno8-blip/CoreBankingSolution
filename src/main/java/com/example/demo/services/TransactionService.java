package com.example.demo.services;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.demo.entities.Transaction;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.AccountMapper;
import com.example.demo.mappers.BalanceMapper;
import com.example.demo.mappers.TransactionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.transactions}")
    private String transactionRoutingKey;

    @Value("${rabbitmq.routing.key.account.balances}")
    private String accountBalanceRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    private List<String> allowedCurrencies;
    private List<String> allowedDirections = List.of("IN", "OUT");

    public List<Transaction> findTransactionsById(Long accountId) {
        List<Long> allAccountIds = accountMapper.findAllIds();

        if (!allAccountIds.contains(accountId))
            throw new ResourceNotFoundException("Account not found!");

        return transactionMapper.findTransactionsById(accountId);
    }

    @TransactionalEventListener
    public Transaction insert(Transaction request) {
        Long currentAcc = request.getAccountId();
        List<Long> allAccountIds = accountMapper.findAllIds();
        if (!allAccountIds.contains(currentAcc))
            throw new ResourceNotFoundException("Account not found!");

        allowedCurrencies = balanceMapper.findAccountsCurrency(currentAcc);

        if (!allowedCurrencies.contains(request.getCurrency()))
            throw new InvalidDataException("Invalid currency!");
        if (!allowedDirections.contains(request.getDirection()))
            throw new InvalidDataException("Invalid direction!");
        if (request.getAmount() < 0)
            throw new InvalidDataException("Invalid amount! Amount is negative.");
        if (request.getDescription().equals(""))
            throw new InvalidDataException("Description missing!");

        double transactionAmount = request.getAmount();

        double totalFunds = balanceMapper.findAmountByAccountIdAndCurrency(request.getCurrency(),
                request.getAccountId());

        System.out.println(transactionAmount);
        System.out.println(totalFunds);

        if (request.getDirection().equals("OUT")) {
            if (transactionAmount > totalFunds)
                throw new InvalidDataException("Insufficient funds on the balance!");
        }

        double newBalanceAmount = request.getDirection().equals("OUT") ? totalFunds - transactionAmount
                : totalFunds + transactionAmount;

        balanceMapper.updateBalanceAmount(newBalanceAmount, request.getCurrency(), request.getAccountId());
        // send text message to RabbitMQ, (it can be changed by json with amount and
        // description: New balance)
        request.setBalanceAmountAfterTransaction(newBalanceAmount);
        transactionMapper.insert(request);
        // send json file to the broker
        rabbitTemplate.convertAndSend(exchange, transactionRoutingKey, request);

        rabbitTemplate.convertAndSend(exchange, accountBalanceRoutingKey,
                "Your new balance: " + newBalanceAmount + " currency");
        return request;
    }
}
