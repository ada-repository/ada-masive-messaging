package com.fioxin.messaging.messaging.Domain.Service;


import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.User;
import com.fioxin.messaging.messaging.Domain.Repository.UserJpaRepository;

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
    
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUser(int id) {
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User actually, User newUser) {
        actually.setName(newUser.getName());
        actually.setDni(newUser.getDni());
        actually.setEmail(newUser.getEmail());
        actually.setPhone(newUser.getPhone());
        actually.setStatus(newUser.isStatus());
        return userRepo.save(actually);
    }

}
