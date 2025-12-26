package com.aqtilink.user_service.repository;

import com.aqtilink.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByClerkId(String clerkId);
    boolean existsByClerkId(String clerkId);
    void deleteByClerkId(String clerkId);
}
