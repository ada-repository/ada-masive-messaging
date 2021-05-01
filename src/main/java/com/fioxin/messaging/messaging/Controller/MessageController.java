package com.fioxin.messaging.messaging.Controller;

import java.util.List;
import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Entity.NotificationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.SendMessageRequest;
import com.fioxin.messaging.messaging.Domain.Service.IMessageService;
import com.fioxin.messaging.messaging.Domain.Service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private IUserService userService;

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
    public SendMessageRequest sendMessage(@RequestBody SendMessageRequest message){
        List<NotificationMessage> messages = message.getMessages();
        int idUser = message.getUser().getId();
        return messageService.sendMessage(messages, idUser);
          
    }
    
    @GetMapping("/prueba/{id}")
    public SendMessageRequest prueba(@PathVariable int id){
       User user = userService.getUser(id);
       List<NotificationMessage> messages = user.getMessages();
       return messageService.sendMessage(messages, id);
    }  
    
}