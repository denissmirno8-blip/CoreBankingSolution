package com.example.demo.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {

    private Long transactionId;
    private Long accountId;
    private double amount;
    private String currency;
    private String direction;
    private String description;
    private double balanceAmountAfterTransaction;

    public Transaction() {

    }

}
