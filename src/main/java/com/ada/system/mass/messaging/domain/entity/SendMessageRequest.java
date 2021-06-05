package com.ada.system.mass.messaging.domain.entity;

import java.util.List;
import lombok.Data;

@Data
public class SendMessageRequest {
    private String codiEmpr;
    private String mensaEmpr;
    private List<ClientesNotification> clientes; 
}
