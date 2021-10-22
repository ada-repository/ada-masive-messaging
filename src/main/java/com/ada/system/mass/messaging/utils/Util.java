/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ada.system.mass.messaging.utils;

import com.ada.system.mass.messaging.domain.entity.ClientesNotification;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import com.ada.system.mass.messaging.domain.entity.Subscription;
import com.twilio.rest.api.v2010.account.Message;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 
 * @author IangX
 */
public class Util {

    public static List<NotificationMessage> mappingSendMessageToNotificationMessage(List<ClientesNotification> clients){
        List<NotificationMessage> converted = new ArrayList<>();
        
        for( ClientesNotification  client : clients){
                 NotificationMessage notification = new NotificationMessage();
                    notification.setCodCli(client.getCodiClie());
                    notification.setReceiverNumber(client.getTelfClie());
                    notification.setMailClie(client.getMailClie());                   
                    notification.setSaldActu(client.getSaldActu());
                    converted.add(notification);
             }
              return converted;
    }

    public static List<NotificationMessage>  sendSmsOwner(int userId, String codEmpresa, int cantListNoti, String phone){

        String[] numbers = phone.split(",");
        String text = "Se han enviado la cantidad de  "+ (numbers.length+cantListNoti) + " mensajes. Incluyendo este en la cuenta.";
        List<NotificationMessage> listNotiOwner = new LinkedList<>();
        /*
        for(String number : numbers){
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("+58"+number), //to
                            new com.twilio.type.PhoneNumber("+12057076733"),      //from
                            text)
                    .create();
            NotificationMessage notification = new NotificationMessage();
            notification.setSid(message.getSid());
            notification.setCreatedAt(LocalDate.now());
            notification.setSubject("unknown");
            notification.setMessage(text);
            notification.setReceiverNumber(phone);
            notification.setUserId(userId);
            notification.setCodCli(codEmpresa);
            notification.setStatus(message.getStatus().toString());
            listNotiOwner.add(notification);
        }
         */
        return listNotiOwner;
    }

    public static boolean isSMS(Subscription s){
        return "SMS".equals(s.getPlan().getCategory().getName())  && s.isStatus() ;
    }

    public static boolean isEmptyNumber(NotificationMessage sms){
        return "".equals(sms.getReceiverNumber()) || sms.getSaldActu() <= 0  ;
    }

    public static List<String> numbersVac(List<NotificationMessage> empty){
        List<String> vac = new ArrayList<>();
        empty.forEach( list -> {
            vac.add(list.getCodCli());
        });
        return vac;
    }

    public static boolean validatePhone(String phone){
        Pattern pattern = Pattern.compile("[0-9]{10}");
        return pattern.matcher(phone).matches();
    }

    public static Map<String, List<String>> formattingPhone(String phone){ // puede venir algo asi 426-4236465,4246424715, 0426-8424872
        Map<String, List<String>> response = new HashMap<>();
        List<String> phones = new ArrayList<>();
        List<String> badPhone = new ArrayList<>();
        String[] numbers = phone.replace("-","").replace(" ", "").split(",");
        for(String tlf : numbers){
            if(tlf.charAt(0) ==  '0'){
                tlf=  tlf.replaceFirst("0","");
            }
            if(tlf.contains("251")){
                badPhone.add(tlf);
                System.out.println("añadido "+tlf);
            }
            else if(Util.validatePhone(tlf)){
                phones.add(tlf);
            }
            else{
                badPhone.add(tlf);
            }
        }
        response.put("Correct", phones);
        response.put("Incorrect", badPhone);
        return response;
    }
    
    
}
