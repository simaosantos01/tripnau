package com.desofs.logging.rabbitmq.events;
import com.desofs.logging.rabbitmq.logs.CreatedLogPayload;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class CreatedLogEvent extends EventMessage {

    private static final String EVENT_TOPIC = "logs";

    private CreatedLogPayload log;

    public CreatedLogEvent(CreatedLogPayload createdLog) {
        super(EVENT_TOPIC);
        this.log = createdLog;
    }
}
