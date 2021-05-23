/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.domain.repository;

import com.ada.system.mass.messaging.domain.entity.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author FioxinCel
 */
@Repository
public interface SubscriptionJpaRepository extends JpaRepository<Subscription, Integer> {
    public List<Subscription> findByPlanIdAndUserId(int idPlan, int idUser);
    public List<Subscription> findByUserIdAndStatus(int idUser, boolean status);
}
