/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ada.system.mass.messaging.domain.entity;

import lombok.Data;

/**
 * 
 * @author IangnX
 */
@Data
public class ClientesNotification {
    
     private String codiClie;
     private String telfClie;
     private Double saldActu;
     private String subject;
     private String mailClie;
}
