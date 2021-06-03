package com.ada.system.mass.messaging.controller;

import java.util.List;

import com.ada.system.mass.messaging.domain.service.IMessageService;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import com.ada.system.mass.messaging.domain.entity.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    
   

    @GetMapping("/all")
    public List<NotificationMessage> getAllMessages(){
        return messageService.getAllMessages();
    }
    @GetMapping("/{id}")
    public NotificationMessage getMessage(@PathVariable int id){
        return messageService.getMessage(id);
    }
    
    
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable int id){
        messageService.deleteMessage(id);
    }

    @GetMapping("/{idUser}/{number}")
    public  List<NotificationMessage> getMessagesByReceiverNumb(@PathVariable int idUser, @PathVariable String number){
        return messageService.findByReceiverNumberAndUserId(idUser, number);
    }

    @GetMapping("/user/{idUser}")
    public List<NotificationMessage> getMessagesByUserId(@PathVariable int idUser){
        return messageService.getMessagesByIdUser(idUser);
    }

    @PostMapping("/save")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest message){       
         Map<String, Object> response = new HashMap<>(); 
         try {
            response = messageService.sendMessage(message);
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