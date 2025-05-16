package com.example.user.repository;

import com.example.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

    @Query(value = "SELECT userId, fullName, email, phone FROM User u WHERE u.email LIKE %:domain", nativeQuery = true)
    ArrayList<User> findUserByEmail(@Param("domain") String domain);

    @Query(value = "SELECT userId, fullName, email, phone FROM User u WHERE u.fullName LIKE %:fullName")
    User findUserByFullName(@Param("fullName") String fullName);
}
