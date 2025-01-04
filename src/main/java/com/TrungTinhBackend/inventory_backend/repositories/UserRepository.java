package com.TrungTinhBackend.inventory_backend.repositories;

import com.TrungTinhBackend.inventory_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
