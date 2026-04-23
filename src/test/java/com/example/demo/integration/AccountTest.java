package com.example.demo.integration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.example.demo.entities.Account;
import com.example.demo.entities.AccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    private AccountRequest request;

    @BeforeEach
    void setUp() {
        request = new AccountRequest();

        request.setCustomerId(1000L);
        request.setCurrencies(List.of("USD", "EUR"));
        request.setCountry("Norway");
    }

    @Test
    void createAccountIntegrationTest() throws Exception {

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .string(org.hamcrest.Matchers.containsString("Account is created and sended to RabbitMQ.")));

        verify(rabbitTemplate, atLeastOnce()).convertAndSend(anyString(), anyString(), any(Account.class));
    }

    @Test
    void getAccountTest() throws Exception {
        mockMvc.perform(get("/api/accounts/1")).andExpect(status().isOk());
    }

    @Test
    void getAccountNotFoundTest() throws Exception {
        mockMvc.perform(get("/api/accounts/10000000")).andExpect(status().isNotFound());
    }

}
