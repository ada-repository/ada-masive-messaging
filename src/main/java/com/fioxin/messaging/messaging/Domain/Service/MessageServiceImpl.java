package com.fioxin.messaging.messaging.Domain.Service;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.Message;
import com.fioxin.messaging.messaging.Domain.Repository.MessageJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements IMessageService{

    @Autowired
    private MessageJpaRepository messageRepo;

    @Override
    public List<Message> getAllMessages() {
       return messageRepo.findAll();
    }

    @Override
    public Message getMessage(int id) {
       return messageRepo.findById(id).orElse(null);
    }

    @Override
    public Message sendMessage(Message message, int idUser) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteMessage(int id) {
       messageRepo.deleteById(id);
        
    }

    @Override
    public List<Message> getMessagesByIdUser(int idUser) {       
        return messageRepo.findByUserId(idUser);
    }

    @Override
    public List<Message> findByReceiverNumberAndUserId(int idUser, String receiverNumber) {       
        return messageRepo.findByReceiverNumberAndUserId(receiverNumber, idUser);
    }

    @Override
    public List<Message> getMessagesByCreatedAt(int idUser, Date createdAt) {
        return messageRepo.findByCreatedAtAndUserId(createdAt, idUser);
    }

    @Override
    public List<Message> getMessagesByStatus(int idUser, String status) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
