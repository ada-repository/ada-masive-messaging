package com.fioxin.messaging.messaging.Domain.Service;

import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.User;

public interface IUserService {
    List<User> getAllUsers();
    User getUser(int id);
    boolean deleteUser(int id);
    User saveUser(User user);
    User updateUser(User actually, User newUser);
}
