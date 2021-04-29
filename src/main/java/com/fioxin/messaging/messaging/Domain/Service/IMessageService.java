package com.fioxin.messaging.messaging.Domain.Service;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.Message;

public interface IMessageService {
    List<Message> getAllMessages();
    Message getMessage(int id);
    Message sendMessage(Message message, int idUser);
    void deleteMessage(int id);
    List<Message> getMessagesByIdUser(int idUser);
    List<Message> findByReceiverNumberAndUserId(int idUser, String receiverNumber);
    List<Message> getMessagesByCreatedAt(int idUser, Date createdAt);
    List<Message> getMessagesByStatus(int idUser, String status);
}
