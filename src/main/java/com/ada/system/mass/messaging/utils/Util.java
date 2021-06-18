/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ada.system.mass.messaging.utils;

import com.ada.system.mass.messaging.domain.entity.ClientesNotification;
import com.ada.system.mass.messaging.domain.entity.NotificationMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author IangX
 */
public class Util {

    public List<NotificationMessage> mappingSendMessageToNotificationMessage(List<ClientesNotification> clients){
        List<NotificationMessage> converted = new ArrayList<>();
        
        for( ClientesNotification  client : clients){
                 NotificationMessage notification = new NotificationMessage();
                    notification.setCodCli(client.getCodiClie());
                    notification.setReceiverNumber(client.getTelfClie());
                    notification.setMailClie(client.getMailClie());
                    System.out.println("Saldo: "+client.getSaldActu());
                    notification.setSaldActu(client.getSaldActu());
                    converted.add(notification);
             }
              return converted;
    }
    
    
}
