package com.fioxin.messaging.messaging.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Entity.NotficationMessage;
import com.fioxin.messaging.messaging.Domain.Entity.SendMessageRequest;
import com.fioxin.messaging.messaging.Domain.Service.IMessageService;

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

    @GetMapping("/all")
    public List<NotficationMessage> getAllMessages(){
        return messageService.getAllMessages();
    }
    @GetMapping("/{id}")
    public NotficationMessage getMessage(@PathVariable int id){
        return messageService.getMessage(id);
    }
    
    
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable int id){
        messageService.deleteMessage(id);
    }

    @GetMapping("/{idUser}/{number}")
    public  List<NotficationMessage> getMessagesByReceiverNumb(@PathVariable int idUser, @PathVariable String number){
        return messageService.findByReceiverNumberAndUserId(idUser, number);
    }

    @GetMapping("/user/{idUser}")
    public List<NotficationMessage> getMessagesByUserId(@PathVariable int idUser){
        return messageService.getMessagesByIdUser(idUser);
    }

    @PostMapping("/save")
    public NotficationMessage sendMessage(@RequestBody SendMessageRequest message){
        List<NotficationMessage> messages = message.getMessages();
        int idUser = message.getUser().getId();
        messageService.sendMessage(messages, idUser);
        return  null;
    }
}
