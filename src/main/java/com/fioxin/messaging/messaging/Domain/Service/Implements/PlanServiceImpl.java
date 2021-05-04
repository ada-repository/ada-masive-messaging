/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Domain.Service.Implements;

import com.fioxin.messaging.messaging.domain.Repository.PlanJpaRepository;
import com.fioxin.messaging.messaging.domain.Service.IPlanService;
import com.fioxin.messaging.messaging.domain.entity.Plan;
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
    
    @Override
    public List<Plan> getAllPlans() {
        return planRepo.findAll();
    }

    @Override
    public Plan getPlanById(int id) {
        return planRepo.findById(id).orElse(null);
    }

    @Override
    public void deletePlan(int id) {
        planRepo.deleteById(id);
    }

    @Override
    public Plan updatePlan(Plan actually, Plan newPlan) {
        actually.setName(newPlan.getName());
        actually.setPaymentType(newPlan.getPaymentType());
        actually.setCategory(newPlan.getCategory());
        actually.setTerm(newPlan.getTerm());
        return planRepo.save(actually);
    }

    @Override
    public Plan savePlan(Plan plan) {
        return planRepo.save(plan);
    }
    
}
