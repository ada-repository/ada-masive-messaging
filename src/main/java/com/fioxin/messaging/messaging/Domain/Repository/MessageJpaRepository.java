package com.fioxin.messaging.messaging.Domain.Repository;

import java.util.Date;
import java.util.List;

import com.fioxin.messaging.messaging.Domain.Entity.NotficationMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpaRepository extends JpaRepository<NotficationMessage,Integer> {
    public List<NotficationMessage> findByUserId(int idUser); //Funciona
    public List<NotficationMessage> findByReceiverNumberAndUserId(String receiverNumber, int idUser); //Funciona
    public List<NotficationMessage> findByCreatedAtAndUserId(Date createdAt,int user);
    public List<NotficationMessage> findByStatusAndUser(String status,int user);
}
