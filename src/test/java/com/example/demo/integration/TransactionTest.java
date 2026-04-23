package com.example.demo.integration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.entities.Account;
import com.example.demo.entities.AccountRequest;
import com.example.demo.entities.Transaction;
import com.example.demo.services.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTest {

    @Autowired
    private AccountService accountService;

    private Account newAcc;
    private Long newAccId;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        AccountRequest request = new AccountRequest();

        request.setCustomerId(1000L);
        request.setCurrencies(List.of("USD", "EUR"));
        request.setCountry("Norway");

        newAcc = accountService.insert(request);
        newAccId = newAcc.getAccountId();
    }

    @Test
    void createTransactionTest() throws Exception {

        Transaction transactionRequest = new Transaction();

        transactionRequest.setAccountId(newAccId);
        transactionRequest.setAmount(100);
        transactionRequest.setCurrency("USD");
        transactionRequest.setDirection("IN");
        transactionRequest.setDescription("createTransactionTest");

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .string(org.hamcrest.Matchers
                                .containsString("Transaction is created and sended to RabbitMQ.")));

        verify(rabbitTemplate, atLeastOnce()).convertAndSend(anyString(), anyString(), any(Transaction.class));
    }

    @Test
    void getTransactionListTest() throws Exception {
        mockMvc.perform(get("/api/transactions/" + newAccId)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    void getTransactionListAccountNotFoundTest() throws Exception {
        mockMvc.perform(get("/api/transactions/10000")).andExpect(status().isNotFound()).andDo(print());
    }
}
