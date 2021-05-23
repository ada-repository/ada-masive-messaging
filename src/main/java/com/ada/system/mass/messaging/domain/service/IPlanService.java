/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.domain.service;

import com.ada.system.mass.messaging.domain.entity.Plan;
import java.util.List;

/**
 *
 * @author FioxinCel
 */
public interface IPlanService {
    
    List<Plan> getAllPlans();
    Plan getPlanById(int id);
    boolean deletePlan(int id);
    Plan updatePlan(Plan actually, Plan newPlan);
    Plan savePlan(Plan plan);
}
