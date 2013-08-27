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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import se.skltp.mt.core.entity.Answer;
import se.skltp.mt.core.entity.MessageStatus;
import se.skltp.mt.core.repository.AnswerRepository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

@Repository
public class JpaAnswerRepository extends DefaultJpaRepository<Answer> implements AnswerRepository {

    @SuppressWarnings("unchecked")
    public List<Answer> findAllForCareUnit(String careUnit) {
        try {
            return entityManager.createNamedQuery("Answer.findAllForCareUnit")
                        .setParameter("careUnit", careUnit)
                        .getResultList();
        } catch(NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Answer> findForCareUnit(String careUnit, int maxResults) {
        try {
            return entityManager.createNamedQuery("Answer.findAllForCareUnit")
                        .setParameter("careUnit", careUnit)
                        .setFirstResult(0)
                        .setMaxResults(maxResults)
                        .getResultList();
        } catch(NoResultException e) {
            return Collections.EMPTY_LIST;
        }
    }

    public Long getNumOfQuestionsForCareUnit(String careUnit) {
        return (Long) entityManager.createNamedQuery("Answer.totalCountForCareUnit")
                        .setParameter("careUnit", careUnit)
                        .getSingleResult();
    }
    
    public int delete(String careUnit, Set<Long> ids) {
        return entityManager.createNamedQuery("Answer.deleteForCareUnitWithIds")
                    .setParameter("careUnit", careUnit)
                    .setParameter("ids", ids)
                    .setParameter("status", MessageStatus.RETRIEVED)
                    .executeUpdate();
    }

}
