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
package se.skltp.messagebox.core.repository.jpa;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

@Repository
public class JpaMessageRepository extends DefaultJpaRepository<Message, Long> implements MessageRepository {

    @SuppressWarnings("unchecked")
    public List<Message> getMessagesForSystem(String systemId, Set<Long> ids) {
        try {
            return entityManager.createNamedQuery("Message.getForSystemWithIds")
                    .setParameter("systemId", systemId)
                    .setParameter("ids", ids)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Message> getAllMessagesForSystem(String systemId) {
        try {
            return entityManager.createNamedQuery("Message.getForSystem")
                    .setParameter("systemId", systemId)
                    .getResultList();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    public Long getNumOfMessagesForSystem(String systemId) {
        return (Long) entityManager.createNamedQuery("Message.totalCountForSystem")
                .setParameter("systemId", systemId)
                .getSingleResult();
    }

    public int delete(String systemId, Set<Long> ids) {
        return entityManager.createNamedQuery("Message.deleteForSystemWithIds")
                .setParameter("systemId", systemId)
                .setParameter("ids", ids)
//                .setParameter("status", MessageStatusType.RETRIEVED)
                .executeUpdate();
    }

}
