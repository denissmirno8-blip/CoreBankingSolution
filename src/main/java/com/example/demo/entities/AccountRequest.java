package com.example.demo.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private Long customerId;
    private String country;
    private List<String> currencies;
}
