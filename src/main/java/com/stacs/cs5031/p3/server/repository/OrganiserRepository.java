package com.stacs.cs5031.p3.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stacs.cs5031.p3.server.model.Organiser;

/**
 * Repository interface for {@link Organiser} entities.
 * Provides CRUD operations and custom query methods for accessing Organiser data.
 * Extends JpaRepository to inherit common data access functionality.
 */
public interface OrganiserRepository extends JpaRepository<Organiser, Integer> {
    
    /**
     * Finds an organiser by their username.
     * 
     * @param username the username to search for
     * @return the organiser with the specified username, or null if not found
     */
    Organiser findByUsername(String username);
}
