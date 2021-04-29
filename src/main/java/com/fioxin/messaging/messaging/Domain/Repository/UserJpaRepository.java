package com.fioxin.messaging.messaging.Domain.Repository;

import com.fioxin.messaging.messaging.Domain.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserJpaRepository
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {

    
}