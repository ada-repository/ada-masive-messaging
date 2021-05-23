package com.ada.system.mass.messaging.domain.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 * User
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
   
    @Column(nullable=false) 
    private String name;

    @Column(nullable=false, unique=true)
    private String dni;
    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false, unique=true)
    private String phone;
    
    @Column(nullable=false)
    private Date createdAt;
    @Column(nullable=false)
    private boolean sendBalance;
    @Column(nullable=false)
    private boolean status  = true;

    @OneToMany(mappedBy = "user")
    private List<NotificationMessage> messages;

    @OneToMany(mappedBy = "user")
    private List<Subscription> subscription;
   
    
}