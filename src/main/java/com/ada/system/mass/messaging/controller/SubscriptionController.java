/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.controller;

import com.ada.system.mass.messaging.domain.entity.Subscription;
import com.ada.system.mass.messaging.domain.service.ISubscriptionService;

import java.util.HashMap;
import java.util.List;
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

/**
 *
 * @author FioxinCel
 */

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
    
    @Autowired
    private ISubscriptionService subService;
    
   @GetMapping("/all") 
   public ResponseEntity<?> getAll(){
       Map<String, Object> response = new HashMap<>();
       List<Subscription> subs = null;
       try {
           subs = subService.getAll();
       } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(subs.isEmpty()){
             response.put("Mensaje", "No existen registros en la Base de Datos");
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);       
        }
        response.put("Subscriptions", subs);
        return new ResponseEntity<>(response, HttpStatus.OK);       
   }
    
   @GetMapping("/{id}")
   public ResponseEntity<?> getById(@PathVariable int id){
        Map<String, Object> response = new HashMap<>();
        Subscription sub = null;
        try {
           sub = subService.getById(id);
       } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(sub == null){
            response.put("Mensaje", "La subscripcion con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("Subscripcion", sub);
        return new ResponseEntity<>(response, HttpStatus.OK);       
    
   }
   
   @PostMapping("/save")
   public ResponseEntity<?> saveSubscription(@RequestBody Subscription subscription){      
       Map<String, Object> response = new HashMap<>();
       Map<String, Object> responseSave = new HashMap<>();
       
       try {
           responseSave = subService.saveSubscription(subscription);
       } catch (DataAccessException e) {
            response.put("Mensaje", "Error al guardar la subscripcion en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       if(responseSave.containsKey("Mensaje")){
           return new ResponseEntity<>(responseSave, HttpStatus.BAD_REQUEST);
       }  
       return new ResponseEntity<>(responseSave, HttpStatus.CREATED);
          
   }
    
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteSubscription(@PathVariable int id){
        Map<String, Object> response = new HashMap<>();
          boolean rpta;
          try {
           rpta = subService.deleteSubscription(id);
       }catch (DataAccessException e) {
            response.put("Mensaje", "Error al eliminar el registro en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         if(!rpta){
               response.put("Mensaje", "La subscripcion no se encuentra en registrada");
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
         }
          response.put("Mensaje","Plan eliminado exitosamente!");
          return new ResponseEntity<>(response, HttpStatus.OK);
   }
   
    public ResponseEntity<?> updateSubscription(@RequestBody Subscription newSubs,@PathVariable int id){
        Subscription actually = subService.getById(id);
        Subscription updated = null;
        Map<String, Object> response = new HashMap<>();
        if(actually == null){
          response.put("Error", " No se pudo editar ,la susbcricion con ID: " + id + ". No existe en la Base de Datos");
          return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
       }
        try {
            updated = subService.updateSubscription(actually, newSubs);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al actualizar la subscripcion en la Base de Datos");
            response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);        
         }
        response.put("Mensaje", "La subscripcion fue actualizada exitosamente!");
        response.put("Subscripcion", updated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    } 
}