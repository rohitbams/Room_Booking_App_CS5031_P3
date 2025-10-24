package com.stacs.cs5031.p3.server.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.stacs.cs5031.p3.server.model.Admin;

/**
 * Integration tests for the {@link AdminRepository} interface.
 * Tests CRUD operations on Admin entities using an in-memory database.
 * The @DataJpaTest annotation configures an in-memory database and JPA repositories for testing.
 */
@DataJpaTest
public class AdminRepositoryTest {
    
    /**
     * The AdminRepository instance being tested, automatically injected by Spring.
     */
    @Autowired
    private AdminRepository adminRepository;

    /**
     * Tests saving an Admin entity to the database.
     * Verifies that:
     * 1. The Admin is saved with a generated ID
     * 2. The saved Admin can be retrieved with correct properties
     */
    @Test
    void testSave() {
        Admin john = new Admin("John", "greatestJohn", "john123");
        adminRepository.save(john);
        int savedId = john.getId();

        Admin result = adminRepository.findById(savedId).orElseThrow();
        assertEquals(savedId, result.getId());
        assertEquals("John", result.getName());
        assertEquals("greatestJohn", result.getUsername());
        assertEquals("john123", result.getPassword());
    }

    /**
     * Tests updating an Admin entity in the database.
     * Verifies that:
     * 1. The Admin is initially saved correctly
     * 2. Updates to the Admin are persisted
     * 3. The updated Admin can be retrieved with modified properties
     */
    @Test
    void testUpdate() {
        Admin john = new Admin("John", "greatestJohn", "john123");
        adminRepository.save(john);
        john.setName("Johnny"); // update name
        adminRepository.save(john); // update name
        int savedId = john.getId();
        
        Admin result = adminRepository.findById(savedId).orElseThrow();
        assertEquals(savedId, result.getId());
        assertEquals("Johnny", result.getName());
        assertEquals("greatestJohn", result.getUsername());
        assertEquals("john123", result.getPassword());
    }
    
    /**
     * Tests finding an Admin by username.
     * Verifies that:
     * 1. The Admin can be retrieved using a custom query method
     * 2. The retrieved Admin has the correct properties
     */
    @Test
    void testFindByUsername() {
        Admin john = new Admin("John", "greatestJohn", "john123");
        adminRepository.save(john);
        
        Optional<Admin> result = adminRepository.findByUsername("greatestJohn");
        
        assertTrue(result.isPresent());
        
        Admin foundAdmin = result.get();
        assertEquals(john.getId(), foundAdmin.getId());
        assertEquals("John", foundAdmin.getName());
        assertEquals("greatestJohn", foundAdmin.getUsername());
        assertEquals("john123", foundAdmin.getPassword());
    }
    
    /**
     * Tests retrieving all Admin entities from the database.
     * Verifies that:
     * 1. Multiple Admin entities can be saved
     * 2. All saved Admin entities can be retrieved in a list
     * 3. The count of retrieved entities matches the number saved
     */
    @Test
    void testFindAll() {
        Admin john = new Admin("John", "greatestJohn", "john123");
        Admin tom = new Admin("Tom", "tom", "tom123");
        Admin sam = new Admin("Sam", "samthegreat", "samisgreat");

        adminRepository.saveAll(List.of(john, tom, sam));
        List<Admin> result = adminRepository.findAll();

        assertEquals(3, result.size());
    }

    /**
     * Tests deleting an Admin entity by ID.
     * Verifies that:
     * 1. An Admin can be saved and then deleted
     * 2. After deletion, the Admin cannot be found by ID
     */
    @Test
    void testDeleteById() {
        Admin john = new Admin("John", "greatestJohn", "john123");
        adminRepository.save(john);
        int id = john.getId();
        adminRepository.deleteById(id);
        Optional<Admin> result = adminRepository.findById(id);
        assertTrue(result.isEmpty());
    }
}
// 5