/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Controller;

import com.fioxin.messaging.messaging.domain.Service.IPlanService;
import com.fioxin.messaging.messaging.domain.entity.Plan;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author FioxinCel
 */

@RestController
@RequestMapping("/plans")
public class PlanController {
    
    @Autowired
    private IPlanService planService;
    
    @GetMapping("/all")
    public  ResponseEntity<?> getAllPlans(){
         Map<String, Object> response = new HashMap<>();
         List<Plan> plans = null;
         try {
            plans = planService.getAllPlans();
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(plans.size() == 0){
             response.put("Mensaje", "No existen registros en la Base de Datos");
             return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
         response.put("Planes", plans);
        return new ResponseEntity<>(response, HttpStatus.OK);  
        }
    
     @GetMapping("/{id}")
     public ResponseEntity<?> getPlanById(@PathVariable int id){
         Plan plan = null;
         Map<String, Object> response = new HashMap<>();
         try {
             plan = planService.getPlanById(id);
         } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(plan == null){
            response.put("Mensaje", "El plan con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("Plan", plan);
        return new ResponseEntity<>(response, HttpStatus.OK);       
     }   
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable int id){
          Map<String, Object> response = new HashMap<>();
          boolean rpta;
         try {
            rpta = planService.deletePlan(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al eliminar el registro en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
         if(!rpta){
             response.put("Mensaje", "El plan que quiere eliminar esta asignado a una subscripcion vigente.");
               return new ResponseEntity<>(response, HttpStatus.CONFLICT);
         }
        response.put("Mensaje","Plan eliminado exitosamente!");
        return new ResponseEntity<>(response, HttpStatus.OK);
  
     }
     
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlan(@RequestBody Plan plan,@PathVariable int id ){
        Map<String, Object> response = new HashMap<>();
        Plan actually = planService.getPlanById(id);
        Plan planUpdated = null;
        if(actually == null){
            response.put("Mensaje", "El plan con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            planUpdated = planService.updatePlan(actually, plan);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al actualizar el plan en la Base de Datos");
            response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);        
        }
        response.put("Mensaje", "El plan fue actualizado exitosamente!");
        response.put("Plan", planUpdated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);     
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> savePlan(@RequestBody Plan plan){
       Map<String, Object> response = new HashMap<>();
       Plan planSaved = null;
       
        try {
            planSaved = planService.savePlan(plan);
        }  catch (DataAccessException e) {
            response.put("Mensaje", "Error al guardar el plan en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje:", "Registro Guardado exitosamente.!");
        response.put("Plan", planSaved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
 
    }
     
}
            
    

