package se.inera.ifv.casebox.core.service;

import java.util.List;

import se.inera.ifv.casebox.core.entity.User;
import se.inera.ifv.casebox.exception.CaseboxException;

/**
 * Service interface for user interactions. Create, edit and delete a user in casebox.
 */
public interface UserService {
    
    /**
     * Create user with username and password. 
     * @param userName
     * @param password
     * @param firstName
     * @param lastName
     * @Return {@link User}
     * @throws CaseboxException throws exception if user is already existing
     */
    User createUser(String userName, String password, String firstName, String lastName) throws CaseboxException;
    
    /**
     * Delete the user with the given id. 
     * @param id
     * @throws CaseboxException throws exception if user is not possible to delete
     */
    void deleteUser(String username) throws CaseboxException;
    
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
