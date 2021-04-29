package com.fioxin.messaging.messaging.Domain.Service;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotficationMessage;

public interface IMessageService {
    List<NotficationMessage> getAllMessages();
    NotficationMessage getMessage(int id);
    NotficationMessage sendMessage(List<NotficationMessage> notficationMessage, int idUser);
    void deleteMessage(int id);
    List<NotficationMessage> getMessagesByIdUser(int idUser);
    List<NotficationMessage> findByReceiverNumberAndUserId(int idUser, String receiverNumber);
    List<NotficationMessage> getMessagesByCreatedAt(int idUser, Date createdAt);
    List<NotficationMessage> getMessagesByStatus(int idUser, String status);
}
