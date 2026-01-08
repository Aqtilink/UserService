package com.aqtilink.user_service.messaging;

import com.aqtilink.user_service.dto.NotificationEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

// Component for publishing notification events to RabbitMQ

@Component
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(NotificationEventDTO dto) {
        rabbitTemplate.convertAndSend("notification-exchange", "notification.routingkey", dto);
    }
    
}
