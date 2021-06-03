package com.ada.system.mass.messaging.domain.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class NotificationMessage {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String codCli;
    private String message;
    private String receiverNumber;
    private LocalDate createdAt;
    private String subject;
    private String sid;
    
    @Column(name = "user_id")
    private Integer userId;
    private String status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    


}
