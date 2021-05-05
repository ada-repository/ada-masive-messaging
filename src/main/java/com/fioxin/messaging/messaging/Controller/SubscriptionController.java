/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Controller;

import com.fioxin.messaging.messaging.domain.Service.ISubscriptionService;
import com.fioxin.messaging.messaging.domain.entity.Subscription;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(subs.size() == 0){
             response.put("Mensaje", "No existen registros en la Base de Datos");
             return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);       
        }
        response.put("Subscriptions", subs);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);       
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
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(sub == null){
            response.put("Mensaje", "La subscripcion con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        response.put("Subscripcion", sub);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);       
    
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
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       if(responseSave.containsKey("Mensaje")){
           return new ResponseEntity<Map<String, Object>>(responseSave, HttpStatus.INTERNAL_SERVER_ERROR);
       }  
       return new ResponseEntity<Map<String, Object>>(responseSave, HttpStatus.CREATED);
          
   }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscription(@RequestBody Subscription newSubs,@PathVariable int id){
        Subscription actually = subService.getById(id);
        Subscription updated = null;
        Map<String, Object> response = new HashMap<>();
        if(actually == null){
          response.put("Error", " No se pudo editar ,la susbcricion con ID: " + id + ". No existe en la Base de Datos");
          return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
       }
        try {
            updated = subService.updateSubscription(actually, newSubs);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al actualizar la subscripcion en la Base de Datos");
            response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);        
         }
        response.put("Mensaje", "La subscripcion fue actualizada exitosamente!");
        response.put("Subscripcion", updated);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
   
    
}


/*

 
    void deleteSubscription(int id); Condiciones para que se vuelva false el estatus
   
*/