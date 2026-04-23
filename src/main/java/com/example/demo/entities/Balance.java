package com.example.demo.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Balance {

    private Long balanceId;
    private double amount;
    private String currency;
    private Long accountId;

    public Balance() {

    }
}