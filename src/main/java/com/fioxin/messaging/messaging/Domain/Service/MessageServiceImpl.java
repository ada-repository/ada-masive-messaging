package com.fioxin.messaging.messaging.Domain.Service;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotficationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Repository.MessageJpaRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements IMessageService{

    public static final String ACCOUNT_SID = "ACa46c31f03e3a56eb1fe96d771c4e8dcb";
    public static final String AUTH_TOKEN = "97442378102fa72cb57185d93df944bf";

    @Autowired
    private MessageJpaRepository messageRepo;

    @Autowired
    private IUserService userService;

    @Override
    public List<NotficationMessage> getAllMessages() {
       return messageRepo.findAll();
    }

    @Override
    public NotficationMessage getMessage(int id) {
       return messageRepo.findById(id).orElse(null);
    }

    

    @Override
    public void deleteMessage(int id) {
       messageRepo.deleteById(id);
        
    }

    @Override
    public List<NotficationMessage> getMessagesByIdUser(int idUser) {       
        return messageRepo.findByUserId(idUser);
    }

    @Override
    public List<NotficationMessage> findByReceiverNumberAndUserId(int idUser, String receiverNumber) {       
        return messageRepo.findByReceiverNumberAndUserId(receiverNumber, idUser);
    }

    @Override
    public List<NotficationMessage> getMessagesByCreatedAt(int idUser, Date createdAt) {
        return messageRepo.findByCreatedAtAndUserId(createdAt, idUser);
    }

    @Override
    public List<NotficationMessage> getMessagesByStatus(int idUser, String status) {
       
        
        return null;
    }
    
    @Override
    public NotficationMessage sendMessage(List<NotficationMessage> notficationMessage, int idUser) {      
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        User user = userService.getUser(idUser);
        for(NotficationMessage mssg : notficationMessage ){     
        Message message = Message.creator(
            new com.twilio.type.PhoneNumber(mssg.getReceiverNumber()), //to
            new com.twilio.type.PhoneNumber("+12057076733"),      //from          
            user.getName() + " Informa: " +  mssg.getMessage())
            .create();
         System.out.println(message.getSid());
        }
        return null;
    }
}
