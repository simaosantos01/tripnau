package com.desofs.logging.rabbitmq;

import com.desofs.logging.enums.LogType;
import com.desofs.logging.rabbitmq.events.CreatedLogEvent;
import com.desofs.logging.rabbitmq.logs.CreatedLogPayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Runner implements CommandLineRunner {

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    private final RabbitTemplate rabbitTemplate;

    public Runner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) {
        CreatedLogEvent createdLogEvent =
                new CreatedLogEvent(new CreatedLogPayload(LocalDateTime.now(), "Olá!", LogType.INFO));
        rabbitTemplate.convertAndSend(exchangeName, "topic.logs", createdLogEvent);
    }
}
