/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.domain.Service;

import com.fioxin.messaging.messaging.domain.entity.Subscription;
import java.util.List;

/**
 *
 * @author FioxinCel
 */
public interface ISubscriptionService {
    
    List<Subscription> getAll();
    Subscription getById(int id);
    Subscription updateSubscription(Subscription actually,Subscription newSubs);
    void deleteSubscription(int id);
    Subscription saveSubscription(Subscription subscription);
}
