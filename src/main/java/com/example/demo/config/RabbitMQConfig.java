package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.accounts}")
    private String accountQueue;

    @Value("${rabbitmq.queue.transactions}")
    private String transactionQueue;

    @Value("${rabbitmq.queue.account.balances}")
    private String accountBalanceQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.accounts}")
    private String accountRoutingKey;
    @Value("${rabbitmq.routing.key.transactions}")
    private String transactionRoutingKey;
    @Value("${rabbitmq.routing.key.account.balances}")
    private String accountBalanceRoutingKey;

    @Bean
    public Queue accountQueue() {
        return new Queue(accountQueue);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(transactionQueue);
    }

    @Bean
    public Queue accountBalanceQueue() {
        return new Queue(accountBalanceQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder
                .bind(accountQueue())
                .to(exchange())
                .with(accountRoutingKey);
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder
                .bind(transactionQueue())
                .to(exchange())
                .with(transactionRoutingKey);
    }

    @Bean
    public Binding accountBalanceBinding() {
        return BindingBuilder
                .bind(accountBalanceQueue())
                .to(exchange())
                .with(accountBalanceRoutingKey);
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
