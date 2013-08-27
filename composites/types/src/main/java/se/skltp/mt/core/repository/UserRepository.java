package se.skltp.mt.core.repository;

import se.skltp.mt.core.entity.User;
import se.vgregion.dao.domain.patterns.repository.Repository;

/**
 * User repository interface. Responsible for handling all user queries. 
 *
 */
public interface UserRepository extends Repository<User, String>  {
    
    User findByUsername(String username);
}
