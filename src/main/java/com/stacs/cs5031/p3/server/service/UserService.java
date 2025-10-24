package com.stacs.cs5031.p3.server.service;

import com.stacs.cs5031.p3.server.exception.UserAlreadyExistsException;
import com.stacs.cs5031.p3.server.exception.UserNotFoundException;
import com.stacs.cs5031.p3.server.model.Attendee;
import com.stacs.cs5031.p3.server.model.Organiser;
import com.stacs.cs5031.p3.server.model.User;
import com.stacs.cs5031.p3.server.repository.UserRepository;
import com.stacs.cs5031.p3.server.dto.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * The UserService class.
 * This class implements user management operations like
 * registering users, checking is username is taken, finding users by IDs,
 * deleting users, and listing all users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method registers a new user based on the registration request.
     * It creates either an Organiser or Attendee based on the role specified.
     *
//     * @param request The registration request containing user details and role
     * @return The saved user entity
     * @throws UserAlreadyExistsException If a user with the requested username already exists
     */
    public User registerUser(RegistrationRequest request) {
        if (isUsernameTaken(request.getUsername())) {
            throw new UserAlreadyExistsException(request.getUsername());
        }

        // if role is 'ORGANISER'
        User newUser;
        if (request.getRole().equals("ORGANISER")) {
            newUser = new Organiser(
                    request.getName(),
                    request.getUsername(),
                    request.getPassword()
            );
        } else {
            newUser = new Attendee(
                    request.getName(),
                    request.getUsername(),
                    request.getPassword()
            );
        }
        return userRepository.save(newUser);
    }

    // check for pre-registered username
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // find user by ID
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // find user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    // delete user
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    // list all registered users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //
}
