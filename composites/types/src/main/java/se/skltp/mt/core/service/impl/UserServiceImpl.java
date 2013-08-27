package se.skltp.mt.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.skltp.mt.core.entity.Authority;
import se.skltp.mt.core.entity.AuthorityRole;
import se.skltp.mt.core.entity.User;
import se.skltp.mt.core.repository.UserRepository;
import se.skltp.mt.core.service.UserService;
import se.skltp.mt.exception.MessageException;



@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public User createUser(String username, String password, String firstName, String lastName) throws MessageException {

        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.error("Username {} already exist, can not create user.", new Object[] {username});
            throw new MessageException("error.user.alreadyexist");
        }
        
        user = new User(username, password, true, firstName, lastName);
        Authority authority = new Authority(user, AuthorityRole.ROLE_USER);
        user.getAuthorities().add(authority);
        
        log.debug("Create user {}.", new Object[]{user});
        user = userRepository.store(user);

        return user;
    }

    public void deleteUser(String username) throws MessageException {
        userRepository.remove(username);
    }

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username =  ((UserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username);
            return user;
        }
        return null;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<User>();
        users.addAll(userRepository.findAll());
        return users;
    }
}
