package com.archpatterns.deliveryordermanager.repository;

import com.archpatterns.deliveryordermanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
