/*
 * Copyright 2010 Inera
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *
 *   Boston, MA 02111-1307  USA
 */
package se.skltp.mt.core.repository.jpa;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import se.skltp.mt.core.entity.User;
import se.skltp.mt.core.repository.UserRepository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.AbstractJpaRepository;

@Repository
public class JpaUsersRepository extends AbstractJpaRepository<User, String, String> implements UserRepository {

    public User findByUsername(String username) {
        try {
            return (User) entityManager.createNamedQuery("User.findByUsername")
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

	public User find(String id) {
		// TODO No idea if this is correct /matso
		return findByUsername(id);
	} 
}
