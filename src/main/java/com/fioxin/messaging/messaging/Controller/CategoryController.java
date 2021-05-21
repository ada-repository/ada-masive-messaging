/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor
 */
package com.fioxin.messaging.messaging.Controller;

import com.fioxin.messaging.messaging.domain.Service.ICategoryService;
import com.fioxin.messaging.messaging.domain.entity.Category;
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
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private ICategoryService cateService;
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllCategory(){
        Map<String, Object> response = new HashMap<>();
        List<Category> cat = null;
        try {
            cat = cateService.getAll();
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(cat.size() == 0){
            response.put("Mensaje", "No existen registros en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("Categorias", cat);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable int id){
        Category cat = null;
        Map<String, Object> response = new HashMap<>();
        try {
            cat = cateService.getCategoryById(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(cat == null){
            response.put("Mensaje", "La categoria con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("Categoria", cat);
        return new ResponseEntity<>(response, HttpStatus.OK);       
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){
         Map<String, Object> response = new HashMap<>();
          boolean rpta;
         try {
           rpta =  cateService.deleteCategory(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al eliminar el registro en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
         if(!rpta){
              response.put("Mensaje", "La categoria que quiere eliminar esta asignada a una subscripcion vigente.");
               return new ResponseEntity<>(response, HttpStatus.CONFLICT);
         }
        response.put("Mensaje","Categoria eliminada exitosamente!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody Category category){
        Map<String, Object> response = new HashMap<>();
        Category categorySaved = null;
        try {
            categorySaved = cateService.saveCategory(category);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al guardar la categoria en la Base de Datos");
            response.put("ERROR", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje:", "Registro Guardado exitosamente.!");
        response.put("Categoria", categorySaved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable int id){
        Map<String, Object> response = new HashMap<>();
        Category catActually = cateService.getCategoryById(id);
        Category catUpdated = null;
        if( catActually == null){
            response.put("Mensaje", "La categoria con el ID: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            catUpdated = cateService.updateCategory(catActually, category);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al actualizar la categoria en la Base de Datos");
            response.put("Error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);        
        }
        response.put("Mensaje", "La categoria fue actualizada exitosamente!");
        response.put("Categoria", catUpdated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }   
}
