/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Domain.Service.Implements;

import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Service.IUserService;
import com.fioxin.messaging.messaging.domain.Repository.SubscriptionJpaRepository;
import com.fioxin.messaging.messaging.domain.Service.IPlanService;
import com.fioxin.messaging.messaging.domain.Service.ISubscriptionService;
import com.fioxin.messaging.messaging.domain.entity.Plan;
import com.fioxin.messaging.messaging.domain.entity.Subscription;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FioxinCel
 */
@Service
public class SubscriptionServiceImpl implements ISubscriptionService {

    @Autowired
    private SubscriptionJpaRepository subsRepo;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IPlanService planService;
    
    @Override
    public List<Subscription> getAll() {
        return subsRepo.findAll();
    }

    @Override
    public Subscription getById(int id) {
        return subsRepo.findById(id).orElse(null);
    }

    @Override
    public Subscription updateSubscription(Subscription actually, Subscription newSubs) {
        actually.setPrice(newSubs.getPrice());
        actually.setEndDate(newSubs.getEndDate());
        return subsRepo.save(actually);
    }

    @Override
    public void deleteSubscription(int id) {
        subsRepo.deleteById(id);
    }

    @Override
    public Map<String, Object> saveSubscription(Subscription subscription) {
        
        Map<String, Object> response = new HashMap<>();
        User user = userService.getUser(subscription.getUserId());      
        if(user == null){
            
            response.put("Mensaje", "Usuario no existe en la Base de datos");
            return response;
        }
        Plan plan = planService.getPlanById(subscription.getPlanId());
        
        if(plan == null){
             response.put("Mensaje", "Plan no existe en la Base de datos");
            return response;
        }
        
        int duration = plan.getTerm(); 
        subscription.setEndDate(subscription.getStartDate().plusDays(duration));
        subscription.setStatus(true);
        Subscription sub = subsRepo.save(subscription);
        response.put("Mensaje", "La subscripcion fue guardado exitosamente!");
        response.put("Subscription", sub);
        return  response;
    }
    
}
