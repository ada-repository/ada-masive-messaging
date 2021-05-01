package com.fioxin.messaging.messaging.Domain.Service;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotificationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.SendMessageRequest;
import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Repository.MessageJpaRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements IMessageService{

    public static final String ACCOUNT_SID = "ACa46c31f03e3a56eb1fe96d771c4e8dcb";
    public static final String AUTH_TOKEN = "6183e6ed35b4e8f1b1d39e7fc3cea749";

    @Autowired
    private MessageJpaRepository messageRepo;

    @Autowired
    private IUserService userService;

    @Override
    public List<NotificationMessage> getAllMessages() {
       return messageRepo.findAll();
    }

    @Override
    public NotificationMessage getMessage(int id) {
       return messageRepo.findById(id).orElse(null);
    }

    

    @Override
    public void deleteMessage(int id) {
       messageRepo.deleteById(id);
        
    }

    @Override
    public List<NotificationMessage> getMessagesByIdUser(int idUser) {       
        return messageRepo.findByUserId(idUser);
    }

    @Override
    public List<NotificationMessage> findByReceiverNumberAndUserId(int idUser, String receiverNumber) {       
        return messageRepo.findByReceiverNumberAndUserId(receiverNumber, idUser);
    }

    @Override
    public List<NotificationMessage> getMessagesByCreatedAt(int idUser, Date createdAt) {
        return messageRepo.findByCreatedAtAndUserId(createdAt, idUser);
    }

    @Override
    public List<NotificationMessage> getMessagesByStatus(int idUser, String status) {
       
        
        return null;
    }
    
    @Override
    public SendMessageRequest sendMessage(List<NotificationMessage> notficationMessage, int idUser) {      
        User user = userService.getUser(idUser);
        String finalMessage = user.getName() + " Informa: " +  notficationMessage.get(0).getMessage();
        SendMessageRequest sendMessage = new SendMessageRequest();
        NotificationMessage notification = new NotificationMessage(); 
        List<NotificationMessage> listNoti = new ArrayList<>();
       
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);     
        sendMessage.setUser(user); //Lo retornaremos en la respuesta de la api
        for(NotificationMessage mssg : notficationMessage ){     
         
            Message message = Message.creator(                    
            new com.twilio.type.PhoneNumber(mssg.getReceiverNumber()), //to
            new com.twilio.type.PhoneNumber("+12057076733"),      //from          
           finalMessage)
            .create();
         notification.setReceiverNumber(mssg.getReceiverNumber());
         notification.setMessage(finalMessage);
         notification.setStatus(message.getStatus().toString());
         notification.setNameReceiver(mssg.getNameReceiver());
         notification.setCreatedAt(Date.from(message.getDateCreated().toInstant()));
         notification.setSubject(mssg.getSubject());
         notification.setUserId(idUser);
         listNoti.add(notification); 
         System.out.println("------------------------------------------------------");
         System.out.println("------------------------------------------------------");

         System.out.println("Enviar Mensaje a:" + mssg.getReceiverNumber());
          System.out.println("------------------------------------------------------");
         System.out.println("SID: "+message.getSid());
          System.out.println("------------------------------------------------------");
         System.out.println("BODY: "+message.getBody());
          System.out.println("------------------------------------------------------");
         System.out.println("ERROR MESSAGE: "+message.getErrorMessage());
          System.out.println("------------------------------------------------------");
         System.out.println("NUM MEDIA: "+message.getNumMedia());
          System.out.println("------------------------------------------------------");
         System.out.println("DATE CREATED: "+message.getDateCreated());
          System.out.println("------------------------------------------------------");
         System.out.println("DIRECCION: "+message.getDirection());
          System.out.println("------------------------------------------------------");
         System.out.println("ESTATUS: "+message.getStatus());
          System.out.println("------------------------------------------------------");
         System.out.println("ERROR CODE: "+message.getErrorCode());
         System.out.println("------------------------------------------------------");
         System.out.println("------------------------------------------------------");
        }
        sendMessage.setUser(user);
        sendMessage.setMessages(listNoti);
        return sendMessage;
    }
    
    public String createMessage(String username, String body){
        String message = username + " Informa: "+ body;
        return message;
    }

    @Override
    public SendMessageRequest prueba(int idUser) {
        SendMessageRequest objeto = new SendMessageRequest();
        User user = userService.getUser(idUser);
        List<NotificationMessage> noti = messageRepo.findByUserId(idUser);
        objeto.setUser(user);
        objeto.setMessages(noti);
        return objeto;
    }
}
/*  System.out.println("SID: "+message.getSid());
         System.out.println("BODY: "+message.getBody());
         System.out.println("ERROR MESSAGE: "+message.getErrorMessage());
         System.out.println("NUM MEDIA: "+message.getNumMedia());
         System.out.println("DATE CREATED: "+message.getDateCreated());
         System.out.println("DIRECCION: "+message.getDirection());
         System.out.println("ESTATUS: "+message.getStatus());
         System.out.println("ERROR CODE: "+message.getErrorCode());
*/