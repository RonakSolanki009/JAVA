package com.test.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.test.user.model.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Long> {
}