package com.fioxin.messaging.messaging.Domain.Entity;

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
