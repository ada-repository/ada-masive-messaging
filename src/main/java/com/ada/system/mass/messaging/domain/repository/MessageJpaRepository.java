package com.ada.system.mass.messaging.domain.repository;

import java.util.Date;
import java.util.List;

import com.ada.system.mass.messaging.domain.entity.NotificationMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpaRepository extends JpaRepository<NotificationMessage,Integer> {
    public List<NotificationMessage> findByUserId(int idUser); //Funciona
    public List<NotificationMessage> findByReceiverNumberAndUserId(String to, int idUser); //Funciona
    public List<NotificationMessage> findByCreatedAtAndUserId(Date createdAt,int user);
    public List<NotificationMessage> findByStatusAndUser(String status,int user);
}
