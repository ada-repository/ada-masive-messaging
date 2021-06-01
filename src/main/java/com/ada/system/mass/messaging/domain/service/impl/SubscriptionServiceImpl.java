/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.domain.service.impl;

import com.ada.system.mass.messaging.domain.entity.User;
import com.ada.system.mass.messaging.domain.service.IUserService;
import com.ada.system.mass.messaging.domain.repository.SubscriptionJpaRepository;
import com.ada.system.mass.messaging.domain.service.IPlanService;
import com.ada.system.mass.messaging.domain.service.ISubscriptionService;
import com.ada.system.mass.messaging.domain.entity.Plan;
import com.ada.system.mass.messaging.domain.entity.Subscription;
import java.time.LocalDate;
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
    public boolean deleteSubscription(int id) {
        Subscription subs = getById(id);
        if(subs!= null){
            subs.setStatus(false);
            subsRepo.save(subs);
            return true;
        }else
            return false;
        
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
        
        List<Subscription> subs = subsRepo.findByPlanIdAndUserId(subscription.getPlanId(), subscription.getUserId());
        if(!subs.isEmpty()){
            boolean rpta = subs.stream().anyMatch(s -> planActiveInSubs(s));
            if(rpta){
                response.put("Mensaje", "El usuario ya cuenta con una subscripcion con este plan. Seleccione otro plan");
                return response;
            }
        }
        
        int duration = plan.getTerm();
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(subscription.getStartDate().plusDays(duration));
        subscription.setStatus(true);
        Subscription sub = subsRepo.save(subscription);
        response.put("Subscription", sub);
        return  response;
    }
    
    private boolean planActiveInSubs(Subscription subs){
        return subs.isStatus() == true && !( subs.getEndDate().isBefore(LocalDate.now()));
    }
    
     public  List<Subscription> findSubscriptionByIdUserAndStatus(int idUser,boolean status){
        return subsRepo.findByUserIdAndStatus(idUser, status);
    }
    
}
