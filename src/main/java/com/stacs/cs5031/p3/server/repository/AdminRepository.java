package com.stacs.cs5031.p3.server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stacs.cs5031.p3.server.model.Admin;

/**
 * Repository interface for Admin entity operations.
 * Provides methods to perform CRUD operations on Admin entities in the database.
 * Extends JpaRepository to inherit common data access operations.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
    /**
     * Finds an admin by their username.
     * 
     * @param username The username to search for
     * @return An Optional containing the admin if found, or an empty Optional if no admin exists with the given username
     */
    Optional<Admin> findByUsername(String username);
}
