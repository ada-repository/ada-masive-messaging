
package com.fioxin.messaging.messaging.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Service.IUserService;

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
 * UserController
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = null;
        Map<String, Object> response = new HashMap<>();
        try {
            users = userService.getAllUsers();
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(users.size() == 0) {
            response.put("Mensaje", "No existen registros en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id){
        User user = null;
        Map<String, Object> response = new HashMap<>();
        try {
            user = userService.getUser(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(user == null) {
            response.put("Mensaje", "EL usuario con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
         return new ResponseEntity<>(user, HttpStatus.OK);
    }     

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        Map<String, Object> response = new HashMap<>();
       
        try {
            userService.deleteUser(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al eliminar al usuario en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       response.put("Mensaje","Usuario eliminado exitosamente!");
       return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
       
    }

    @PostMapping("/save")
    public   ResponseEntity<?>  saveUser(@RequestBody User user){
        Map<String, Object> response = new HashMap<>();
        User userInserted = null;
        try {
            userInserted =userService.saveUser(user);
        }catch (DataAccessException e) {
            response.put("Mensaje", "Error al guardar al usuario en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje", "El usuario fue guardado exitosamente!");
        response.put("Usuario",userInserted);
       return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable int id){
        
        User actually = userService.getUser(id);    
        User updated = null;
        Map<String, Object> response = new HashMap<>();  
        if (actually == null) {
           response.put("Error", " No se pudo editar , el usuario con ID: " + id + ". No existe en la Base de Datos");
           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
       }
       try {

           updated =  userService.updateUser(actually, newUser);

       } catch (DataAccessException e) {
        response.put("Mensaje", "Error al actualizar el usuario en la Base de Datos");
        response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);        
    }
    response.put("Mensaje", "El Usurario fue actualizado exitosamente!");
    response.put("Usurario", updated);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
}