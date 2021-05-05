package com.fioxin.messaging.messaging.Domain.Service;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotificationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.SendMessageRequest;
import java.util.Map;

public interface IMessageService {
    List<NotificationMessage> getAllMessages();
    NotificationMessage getMessage(int id);
    Map<String, Object> sendMessage(SendMessageRequest message);
    void deleteMessage(int id);
    List<NotificationMessage> getMessagesByIdUser(int idUser);
    List<NotificationMessage> findByReceiverNumberAndUserId(int idUser, String receiverNumber);
    List<NotificationMessage> getMessagesByCreatedAt(int idUser, Date createdAt);
    List<NotificationMessage> getMessagesByStatus(int idUser, String status);
}
