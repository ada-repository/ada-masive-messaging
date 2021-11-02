package com.ada.system.mass.messaging.controller;

import java.util.List;

import com.ada.system.mass.messaging.domain.service.IMessageService;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import com.ada.system.mass.messaging.domain.entity.SendMessageRequest;
import com.ada.system.mass.messaging.utils.Util;

import java.util.HashMap;
import java.util.Map;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Value("${twilio.account.sid}")
    public String ACCOUNT_SID;
    @Value("${twilio.account.token}")
    public String AUTH_TOKEN;
    @Value("${twilio.account.numberPhone}")
    public String PHONE_NUMBER;

    @Autowired
    private IMessageService messageService;
    
   Util util = new Util();

    @GetMapping("/all")
    public ResponseEntity<?> getAllMessages(){
        Map<String, Object> response = new HashMap<>();
        List<NotificationMessage> messages = null;
        try {
            messages =  messageService.getAllMessages();
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(messages.isEmpty()){
             response.put("Mensaje", "No existen registros en la Base de Datos");
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);       
        }
        response.put("Messages", messages);
        return new ResponseEntity<>(response, HttpStatus.OK);     
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessage(@PathVariable int id){
        Map<String, Object> response = new HashMap<>();
        NotificationMessage sms = null;
        try {
            sms =  messageService.getMessage(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(sms == null){
            response.put("Mensaje", "La el mensaje con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("Message", sms);
        return new ResponseEntity<>(response, HttpStatus.OK); 
       
    }
    
    @PostMapping("/save")
        public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request){       
         Map<String, Object> response = new HashMap<>();        
         System.out.println("Mensaje:"+request.getReporte().get(0).getMensaEmpr());
         System.out.println("Id:"+request.getReporte().get(0).getCodiEmpr());
         List<NotificationMessage> messages =  Util.mappingSendMessageToNotificationMessage(request.getReporte().get(0).getClientes());
         try {
           response = messageService.sendMessage(request.getReporte().get(0).getCodiEmpr(),request.getReporte().get(0).getMensaEmpr(),messages);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
          if(response.containsKey("Mensaje")){
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
       }  
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping("/find-status/{grupoSms}")
    public ResponseEntity<Message> findStatusMessageList(@PathVariable int grupoSms){
        String sid = "SMa355da24624e40a2ab1696e743c35c75";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message2 = Message.fetcher(sid).fetch();
        return new ResponseEntity<>(message2, HttpStatus.OK);
    }
    
}