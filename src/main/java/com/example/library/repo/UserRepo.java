package com.example.library.repo;

import com.example.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    
    User findByUsername(String username);

    boolean existsByUsername(String username);
    
}
