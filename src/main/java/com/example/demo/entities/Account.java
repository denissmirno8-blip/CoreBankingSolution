package com.example.demo.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private Long accountId;
    private Long customerId;
    private List<Balance> balances;

    public Account() {

    }
}
