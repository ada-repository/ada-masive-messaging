package com.fioxin.messaging.messaging.Domain.Repository;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpaRepository extends JpaRepository<Message,Integer> {
    public List<Message> findByUserId(int idUser); //Funciona
    public List<Message> findByReceiverNumberAndUserId(String receiverNumber, int idUser); //Funciona
    public List<Message> findByCreatedAtAndUserId(Date createdAt,int user);
    public List<Message> findByStatusAndUser(String status,int user);
}
