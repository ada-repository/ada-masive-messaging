package com.fioxin.messaging.messaging.Controller;

import java.util.List;
import com.fioxin.messaging.messaging.Domain.Entity.NotificationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.SendMessageRequest;
import com.fioxin.messaging.messaging.Domain.Service.IMessageService;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
        response = messageService.sendMessage(message);
        return new ResponseEntity<Map<String, Object>>(response,HttpStatus.CREATED);
          
    }    
}