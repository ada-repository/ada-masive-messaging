package com.ada.system.mass.messaging.controller;

import java.util.List;

import com.ada.system.mass.messaging.domain.service.IMessageService;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import com.ada.system.mass.messaging.domain.entity.Reporte;
import com.ada.system.mass.messaging.domain.entity.SendMessageRequest;
import com.ada.system.mass.messaging.utils.Util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
    
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
         System.out.println("Mensaje:"+request.getReporte().getMensaEmpr());
         System.out.println("Id:"+request.getReporte().getCodiEmpr());
         List<NotificationMessage> messages =  util.mappingSendMessageToNotificationMessage(request.getReporte().getClientes());
         try {
            response = messageService.sendMessage(request.getReporte().getCodiEmpr(),request.getReporte().getMensaEmpr(),messages);
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
    
}