package com.desofs.logging.rabbitmq.logs;

import com.desofs.logging.enums.LogType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedLogPayload {

    private LocalDateTime datetime;
    private String message;
    private LogType logType;
}
