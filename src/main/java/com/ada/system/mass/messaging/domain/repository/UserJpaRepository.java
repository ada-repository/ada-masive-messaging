package com.ada.system.mass.messaging.domain.repository;

import com.ada.system.mass.messaging.domain.entity.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserJpaRepository
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer> {
 Optional<User> findByOriginCod(String originCod);
    
}