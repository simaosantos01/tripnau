package com.desofs.logging.rabbitmq.events;

import com.desofs.logging.model.Log;
import com.desofs.logging.repositories.ILogRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Getter
@Component
@Slf4j
public class LogEventConsumer {

    private final ILogRepository logRepository;

    public LogEventConsumer(ILogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @RabbitListener(queues = "#{queue.name}")
    private void receiveQueue(EventMessage logMessage) {
        CreatedLogEvent createdLogEvent = (CreatedLogEvent) logMessage;
        Log log = new Log(
                createdLogEvent.getLog().getDatetime(),
                createdLogEvent.getLog().getMessage(),
                createdLogEvent.getLog().getLogType());
        logRepository.save(log);
    }
}