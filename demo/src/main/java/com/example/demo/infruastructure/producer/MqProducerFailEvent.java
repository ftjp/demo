package com.example.demo.infruastructure.producer;

import org.springframework.context.ApplicationEvent;

/**
 * @author caili
 * @create 2024/11/8 15:01
 */
public class MqProducerFailEvent extends ApplicationEvent {
    private final String message;

    public MqProducerFailEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}