package com.ada.system.mass.messaging.domain.service.impl;

import java.util.Date;
import java.util.List;

import com.ada.system.mass.messaging.domain.repository.MessageJpaRepository;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import com.ada.system.mass.messaging.domain.entity.SendMessageRequest;
import com.ada.system.mass.messaging.domain.entity.User;
import com.ada.system.mass.messaging.domain.service.IMessageService;
import com.ada.system.mass.messaging.domain.service.IUserService;
import com.ada.system.mass.messaging.domain.service.IPlanService;
import com.ada.system.mass.messaging.domain.entity.Subscription;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements IMessageService{
    
    @Value("${twilio.account.sid}")
    public String ACCOUNT_SID;
    @Value("${twilio.account.token}")
    public String AUTH_TOKEN;

    @Autowired
    private MessageJpaRepository messageRepo;

    @Autowired
    private IUserService userService;
    
    @Autowired
    private IPlanService planService;
    

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
    public Map<String, Object> sendMessage(SendMessageRequest messages) {      
        Map<String, Object> response = new HashMap<>();      
        User user = userService.getUser(messages.getIdUser());
        String finalMessage = messages.getMessage();
        if(user == null){
            response.put("Mensaje", "Usuario no existe en la Base de datos");
            return response;
        }
        
        //Validamos la Subscripcion del usuario (que sea sms, este activa y este dentro de la fecha)
       boolean rpta = user.getSubscription().stream()
               .anyMatch( s -> isSMS(s));           

       if(!rpta ){
               response.put("Mensaje", "El usuario no tiene subscripcion desponible para esta opcion ");
               return response;
       }

        SendMessageRequest sendMessage = new SendMessageRequest();       
        List<NotificationMessage> listNoti = new LinkedList<>();     
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);  
        
       List<NotificationMessage> empty =  messages.getMessages()
               .stream()
               .filter( sms -> isEmptyNumber(sms)).collect(Collectors.toList());
        
       if(empty.size() >0){
           List<String> vac = new ArrayList<>();
           empty.forEach( list -> {
               vac.add(list.getCodCli());
           });
            response.put("Estos usuarios no tienen numero de telefono asociados", vac);
            return response;
       }
       messages.getMessages().forEach( (sms) -> {
           
             String[] numbers = sms.getReceiverNumber().split(",");   
                for(String number : numbers ){
                    Message message = Message.creator(                    
                    new com.twilio.type.PhoneNumber(number), //to
                    new com.twilio.type.PhoneNumber("+12057076733"),      //from          
                    finalMessage)
                     .create();
                    NotificationMessage notification = new NotificationMessage();           
                    notification.setReceiverNumber(number);
                    notification.setMessage(finalMessage);
                    notification.setStatus(message.getStatus().toString());
                    notification.setCodCli(sms.getCodCli());
                    notification.setCreatedAt(Date.from(message.getDateCreated().toInstant()));
                    notification.setSubject(sms.getSubject());
                    notification.setUserId(user.getId());  
                    notification.setSid(message.getSid());
                    listNoti.add(notification);  
                  }                
          }           
       ); 
       
      NotificationMessage messageOwner = sendSmsOwner(user.getId(), listNoti.size(), user.getPhone());
            listNoti.add(messageOwner);
            sendMessage.setIdUser(user.getId());
            sendMessage.setMessage(finalMessage);            
            sendMessage.setMessages(listNoti);
            messageRepo.saveAll(listNoti);
            response.put("Mensajes", sendMessage);
            response.put("Mensajes", "Mensajes Enviados: "+listNoti.size());   
            return response;
    }
    
    private NotificationMessage  sendSmsOwner(int userId,int cantidad, String phone){
            String Subject = "Reporte al Dueño"; 
            String text = "Se han enviado "+ cantidad+1 + " mensajes. Incluyendo este en la cuenta.";
            Message message = Message.creator(                    
                        new com.twilio.type.PhoneNumber(phone), //to
                        new com.twilio.type.PhoneNumber("+12057076733"),      //from          
                        text)
                         .create();
             NotificationMessage notification = new NotificationMessage(); 
             notification.setSid(message.getSid());
             notification.setCreatedAt(Date.from(message.getDateCreated().toInstant()));
             notification.setSubject(Subject);
             notification.setMessage(text);
             notification.setReceiverNumber(phone);
             notification.setUserId(userId);
             notification.setCodCli(null);
             return notification;
         
    }
    
    private boolean isSMS(Subscription s){
        return "SMS".equals(s.getPlan().getCategory().getName()) && s.isStatus() == true && !( s.getEndDate().isBefore(LocalDate.now())) ;    
    }
    
    private boolean isEmptyNumber(NotificationMessage sms){
        return "".equals(sms.getReceiverNumber());
    } 
}
