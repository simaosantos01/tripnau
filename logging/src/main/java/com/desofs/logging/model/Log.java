package com.desofs.logging.model;

import com.desofs.logging.enums.LogType;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document
@Getter
public class Log {

    @Id
    private final String id;

    private final LocalDateTime datetime;

    private final String message;

    private final LogType logType;

    public Log(LocalDateTime datetime, String message, LogType logType) {
        this.id = UUID.randomUUID().toString();
        this.datetime = datetime;
        this.message = message;
        this.logType = logType;
    }
}
