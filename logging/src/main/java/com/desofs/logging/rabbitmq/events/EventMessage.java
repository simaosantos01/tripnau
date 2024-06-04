package com.desofs.logging.rabbitmq.events;

import com.desofs.logging.rabbitmq.config.ApplicationContextProvider;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "topic")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreatedLogEvent.class, name = "topic.logs")
})
public class EventMessage {

    private String messageId;
    private LocalDateTime timestamp;
    private String topic;
    private String sender;

    public EventMessage(String topic) {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.topic = topic;
        this.sender = ApplicationContextProvider.getContext().getBean(UUID.class).toString();
    }
}
