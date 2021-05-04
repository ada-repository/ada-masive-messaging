/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Controller;

import com.fioxin.messaging.messaging.domain.Service.ISubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    
}
