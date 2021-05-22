package com.fioxin.messaging.messaging.Domain.Service.Implements;


import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Repository.UserJpaRepository;
import com.fioxin.messaging.messaging.Domain.Service.IUserService;
import com.fioxin.messaging.messaging.domain.Service.ISubscriptionService;
import com.fioxin.messaging.messaging.domain.entity.Subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FioxinCel
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserJpaRepository userRepo;
    
    @Autowired
    private ISubscriptionService subService;
    
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUser(int id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public boolean deleteUser(int id) {
        User user = userRepo.findById(id).get();
        if(user !=null){
            List<Subscription> subs = subService.findSubscriptionByIdUserAndStatus(id, true);
            if(!subs.isEmpty()){
                System.out.println("tamaño: "+subs.size());
                subs.forEach( s -> disableSubscription(s));               
             } 
             user.setStatus(false);    
             userRepo.save(user);
              return true;
        }   
        return false;
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User actually, User newUser) {
        actually.setName(newUser.getName());
        actually.setEmail(newUser.getEmail());
        actually.setPhone(newUser.getPhone());
        actually.setSendBalance(newUser.isSendBalance());
        return userRepo.save(actually);
    }
    
   private void disableSubscription(Subscription sub){
       subService.deleteSubscription(sub.getId());
   }

}
