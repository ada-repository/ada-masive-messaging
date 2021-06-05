package com.ada.system.mass.messaging.domain.service;

import java.util.List;

import com.ada.system.mass.messaging.domain.entity.User;

public interface IUserService {
    List<User> getAllUsers();
    User getUser(int id);
    boolean deleteUser(int id);
    User saveUser(User user);
    User updateUser(User actually, User newUser);
    User getUserByOriginCod(String OriginCod);
}
