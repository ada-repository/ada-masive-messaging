/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.domain.Repository;

import com.fioxin.messaging.messaging.domain.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author FioxinCel
 */
@Repository
public interface SubscriptionJpaRepository extends JpaRepository<Subscription, Integer> {
    
}
