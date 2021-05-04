/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Domain.Service.Implements;

import com.fioxin.messaging.messaging.domain.Repository.SubscriptionJpaRepository;
import com.fioxin.messaging.messaging.domain.Service.ISubscriptionService;
import com.fioxin.messaging.messaging.domain.entity.Subscription;
import java.util.List;
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
    public Subscription saveSubscription(Subscription subscription) {
        return subsRepo.save(subscription);
    }
    
}
