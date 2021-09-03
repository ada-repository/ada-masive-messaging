/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ada.system.mass.messaging.controller;

import com.ada.system.mass.messaging.domain.service.IHomeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Xiangn Rodriguez<xiangnrodriguez@gmail.com>
 */

@RestController
@RequestMapping("/home")
public class HomeController {
    
    @Autowired
    private IHomeService home;
    
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public List<String> home(){
         return  home.infoCompany();     
    }    
}
