package com.ada.system.mass.messaging.domain.entity;

import java.util.List;
import lombok.Data;

@Data
public class SendMessageRequest {
    private Integer idUser;
    private String message;
    private List<NotificationMessage> messages;



    public SendMessageRequest() {
    }
}
