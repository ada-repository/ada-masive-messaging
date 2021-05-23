/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.domain.service.impl;
import com.ada.system.mass.messaging.domain.repository.PlanJpaRepository;
import com.ada.system.mass.messaging.domain.service.IPlanService;
import com.ada.system.mass.messaging.domain.service.ISubscriptionService;
import com.ada.system.mass.messaging.domain.entity.Plan;
import com.ada.system.mass.messaging.domain.entity.Subscription;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FioxinCel
 */
@Service
public class PlanServiceImpl implements IPlanService{

    @Autowired
    private PlanJpaRepository planRepo;
    
     @Autowired
    private ISubscriptionService subService;
    
    @Override
    public List<Plan> getAllPlans() {
        return planRepo.findAll();
    }

    @Override
    public Plan getPlanById(int id) {
        return planRepo.findById(id).orElse(null);
    }

    
    //En revision de la validacion de la subscripcion. Por estatus o por fecha?
    @Override
    public boolean deletePlan(int id) {
        //Podria buscar las subscripciones por idPlan
         List<Subscription> listSubs = subService.getAll();
        boolean rpta = listSubs.stream().anyMatch(  s -> planActiveInSub(s,id));
        if(!rpta){
            Plan plan = planRepo.findById(id).get();
            plan.setStatus(false);
            planRepo.save(plan);
            return true;
        }
        return false;
    }

    @Override
    public Plan updatePlan(Plan actually, Plan newPlan) {
        actually.setName(newPlan.getName());
        actually.setPaymentType(newPlan.getPaymentType());
        actually.setCategoryId(newPlan.getCategoryId());
        actually.setTerm(newPlan.getTerm());
        return planRepo.save(actually);
    }

    @Override
    public Plan savePlan(Plan plan) {
        return planRepo.save(plan);
    }
    
    private boolean planActiveInSub(Subscription subs, int id){
        return subs.getPlanId() == id && subs.isStatus() == true && !( subs.getEndDate().isBefore(LocalDate.now()));
    }
    
}
