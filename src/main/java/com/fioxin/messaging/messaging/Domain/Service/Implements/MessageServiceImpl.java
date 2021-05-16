package com.fioxin.messaging.messaging.Domain.Service.Implements;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotificationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.SendMessageRequest;
import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Repository.MessageJpaRepository;
import com.fioxin.messaging.messaging.Domain.Service.IMessageService;
import com.fioxin.messaging.messaging.Domain.Service.IUserService;
import com.fioxin.messaging.messaging.domain.Service.IPlanService;
import com.fioxin.messaging.messaging.domain.entity.Subscription;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements IMessageService{

    public static final String ACCOUNT_SID = "ACa46c31f03e3a56eb1fe96d771c4e8dcb";
    public static final String AUTH_TOKEN = "972dabfef8df7547468d442505c5bf0a";

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
        //Validamos la Subscripcion del usuariom (que sea sms, este activa y este dentro de la fecha)
    boolean rpta = user.getSubscription().stream()
            .anyMatch( s -> isSMS(s));           
      
    if(!rpta ){
            response.put("Mensaje", "El usuario no tiene subscripcion desponible para esta opcion ");
            return response;
    }
   
       //Validamos el tamaño del mensaje
    if( finalMessage.length() > 160){
          response.put("Mensaje", "El tamaño del mensaje excede los 160 caracteres.");
            return response;
    }
    
        SendMessageRequest sendMessage = new SendMessageRequest();
        
        List<NotificationMessage> listNoti = new LinkedList<>();     
        //Comenzamos a enviar los mensajes YUPIII!!
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);     
    //    sendMessage.setUser(user); //Lo retornaremos en la respuesta de la api
    
    
       messages.getMessages().forEach( (sms) -> {
               Message message = Message.creator(                    
               new com.twilio.type.PhoneNumber(sms.getReceiverNumber()), //to
               new com.twilio.type.PhoneNumber("+12057076733"),      //from          
               finalMessage)
               .create();
              NotificationMessage notification = new NotificationMessage();           
               notification.setReceiverNumber(sms.getReceiverNumber());
               notification.setMessage(finalMessage);
               notification.setStatus(message.getStatus().toString());
               notification.setNameReceiver(sms.getNameReceiver());
               notification.setCreatedAt(Date.from(message.getDateCreated().toInstant()));
               notification.setSubject(sms.getSubject());
               notification.setUserId(user.getId());  
               listNoti.add(notification); 
             }                         
       );
       
      sendMessage.setIdUser(user.getId());
      sendMessage.setMessage(finalMessage);            
      sendMessage.setMessages(listNoti);
      messageRepo.saveAll(listNoti);
      response.put("Mensajes", "Mensajes Enviados: "+listNoti.size());
      response.put("Mensajes", sendMessage);
      return response;
    }
    
    
    private boolean isSMS(Subscription s){
        return "SMS".equals(s.getPlan().getCategory().getName()) && s.getStatus() == true && !(LocalDate.parse(s.getEndDate().toString()).isBefore(LocalDate.parse(LocalDate.now().toString()))) ;
       
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