/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.domain.entity;

import java.util.List;
import lombok.Data;

/**
 *
 * @author Personal
 */
@Data
public class Reporte {
    private String codiEmpr;
    private String mensaEmpr;
    private String telefonoReporte;
    private List<ClientesNotification> clientes; 
}
