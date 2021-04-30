package com.fioxin.messaging.messaging.Domain.Repository;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotificationMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpaRepository extends JpaRepository<NotificationMessage,Integer> {
    public List<NotificationMessage> findByUserId(int idUser); //Funciona
    public List<NotificationMessage> findByReceiverNumberAndUserId(String receiverNumber, int idUser); //Funciona
    public List<NotificationMessage> findByCreatedAtAndUserId(Date createdAt,int user);
    public List<NotificationMessage> findByStatusAndUser(String status,int user);
}
