package com.ada.system.mass.messaging.domain.entity;

import java.util.List;
import lombok.Data;

@Data
public class SendMessageRequest {
    private Integer codiEmpr;
    private String mensaEmpr;
    private List<NotificationMessage> messages;
    
    public SendMessageRequest() {
    }
}
