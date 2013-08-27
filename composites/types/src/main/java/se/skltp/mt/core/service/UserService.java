package se.skltp.mt.core.service;

import java.util.List;

import se.skltp.mt.core.entity.User;
import se.skltp.mt.exception.MessageException;

/**
 * Service interface for user interactions. Create, edit and delete a user in the system.
 */
public interface UserService {
    
    /**
     * Create user with username and password. 
     * @param userName
     * @param password
     * @param firstName
     * @param lastName
     * @Return {@link User}
     * @throws MessageException throws exception if user is already existing
     */
    User createUser(String userName, String password, String firstName, String lastName) throws MessageException;
    
    /**
     * Delete the user with the given id. 
     * @param id
     * @throws MessageException throws exception if user is not possible to delete
     */
    void deleteUser(String username) throws MessageException;
    
    /**
     * Returns the username of the authenticated user. 
     * @return String the username
     */
    User getAuthenticatedUser();
    
    /**
     * Returns a list with all users in the system. 
     * @return {@link List} 
     */
    List<User> findAllUsers(); 
    
}
