 package com.ada.system.mass.messaging.domain.service.impl;

import java.net.URI;
import java.util.Date;
import java.util.List;

import com.ada.system.mass.messaging.domain.repository.MessageJpaRepository;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import com.ada.system.mass.messaging.domain.entity.Reporte;
import com.ada.system.mass.messaging.domain.entity.User;
import com.ada.system.mass.messaging.domain.service.IMessageService;
import com.ada.system.mass.messaging.domain.service.IUserService;
import com.ada.system.mass.messaging.domain.service.IPlanService;
import com.ada.system.mass.messaging.domain.entity.Subscription;
import com.ada.system.mass.messaging.utils.Util;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.twilio.rest.api.v2010.account.message.Feedback;
import com.twilio.rest.api.v2010.account.message.FeedbackCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MessageServiceImpl implements IMessageService{
    
    @Value("${twilio.account.sid}")
    public String ACCOUNT_SID;
    @Value("${twilio.account.token}")
    public String AUTH_TOKEN;
    @Value("${twilio.account.numberPhone}")
    public String PHONE_NUMBER;

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
    public Map<String, Object> sendMessage(String codEmpresa, String finalMessage,List<NotificationMessage> messages) {      
        Map<String, Object> response = new HashMap<>();      
        User user = userService.getUserByOriginCod(codEmpresa);
         List<String>  vacios =null ;
        if(user == null){
            response.put("Mensaje", "Usuario no existe en la Base de datos");
            return response;
        }
        
     //Validamos la Subscripcion del usuario (que sea sms, este activa y este dentro de la fecha)
    boolean rpta = user.getSubscription().stream()
            .anyMatch( s -> Util.isSMS(s));
      
    if(!rpta ){
            response.put("Mensaje", "El usuario no tiene subscripcion desponible para esta opcion ");
            return response;
    }
       // Necesitamos un listado de los mensajes que vengan con el numero en blanco o con el saldo en negativo ya que a 
       // estos no se le enviara el sms y asi no habra error en twilio (lo del saldo es a peticion del cliente)
       List<NotificationMessage> empty =  messages.stream()
                                                          .filter( sms -> Util.isEmptyNumber(sms))
                                                          .collect(Collectors.toList());
        
       if(empty.size() >0){
          vacios = Util.numbersVac(empty);
       }
       
       //Filtramos la lista para obtener a los mensajes con numero de telefono y ademas con saldo positivo
       List<NotificationMessage> finalsms = messages.stream()
                                                            .filter(sms -> !Util.isEmptyNumber(sms))
                                                            .collect(Collectors.toList());
         
        List<NotificationMessage> listNoti = new LinkedList<>();     
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        finalsms.forEach( (sms) -> {
            //Necesitamos 2 lista. 1 Con los numeros con formato bueno "Correct" y otro con los que no cumplen con el formato "Incorrect"
             Map<String,List<String>> numbers = Util.formattingPhone(sms.getReceiverNumber());
             if(numbers.get("Incorrect").size() > 0){    
            response.put("Incorrect", "Por favor corrija los siguientes numeros: "+numbers.get("Incorrect"));
            }
            if(numbers.get("Correct").size() > 0){

                for(String number : numbers.get("Correct")){
                    Message message = Message.creator(                    
                    new com.twilio.type.PhoneNumber("+58"+number), //to
                    new com.twilio.type.PhoneNumber(PHONE_NUMBER),      //from          
                    finalMessage)
                     .create();
                    NotificationMessage notification = new NotificationMessage();           
                    notification.setReceiverNumber(number);
                    notification.setMessage(finalMessage);
                    notification.setStatus(message.getStatus().toString());
                    notification.setCodCli(sms.getCodCli());
                    notification.setCreatedAt(LocalDate.now());
                    notification.setSubject(sms.getSubject());
                    notification.setUserId(user.getId());  
                    notification.setSid(message.getSid());
                    listNoti.add(notification);
                  }  
            }
          }           
       );
      if(listNoti.size() > 0){
          List<NotificationMessage> messagesOwners = Util.sendSmsOwner(user.getId(),codEmpresa,listNoti.size(), user.getPhone());
          for(NotificationMessage msgOwner :messagesOwners ){
          listNoti.add(msgOwner);
        }
          messageRepo.saveAll(listNoti);
          if (vacios == null){
          response.put("Información", "Mensajes Enviados: "+listNoti.size());
        }else{
          response.put("Información", "Mensajes Enviados: "+listNoti.size()+ " \n No se enviaron a : "+vacios);
          }
         }else{
          response.put("Información", "Error al enviar mensaje(s). Verifique los números o los saldos");
        }
      return response;
    }
}
