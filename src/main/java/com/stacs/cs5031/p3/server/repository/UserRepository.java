package com.stacs.cs5031.p3.server.repository;

import com.stacs.cs5031.p3.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository class.
 * This class directly interfaces with the database and extends the JpaRepository class.
 * which contains methods for querying, saving, updating, and deleting data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
