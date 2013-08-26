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

package se.inera.ifv.casebox.core.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.ifv.casebox.core.entity.Question;
import se.inera.ifv.casebox.core.repository.QuestionRepository;
import se.inera.ifv.casebox.util.JpaRepositoryTestBase;


/**
 * @author Pär Wenåker
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class OverviewServiceTest extends JpaRepositoryTestBase {

    @Autowired
    OverviewService overviewService;
    
    @Autowired
    QuestionRepository questionRepository;
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Test
    public void testGetCareUnitInfos() throws Exception {
        questionRepository.persist(new Question("careUnit1", "message"));
        questionRepository.persist(new Question("careUnit1", "message"));
        questionRepository.persist(new Question("careUnit1", "message"));
        Question q = new Question("careUnit1", "message");
        q.setStatusRetrieved();
        questionRepository.persist(q);
        questionRepository.persist(new Question("careUnit2", "message"));
        questionRepository.persist(new Question("careUnit2", "message"));
        
        entityManager.flush();
        entityManager.clear();
        
        List<CareUnitInfo> info = new ArrayList<CareUnitInfo>(overviewService.getCareUnitInfos(5));
        
        assertEquals(2, info.size());
        assertEquals("careUnit1", info.get(0).getName());
        assertEquals("careUnit2", info.get(1).getName());
        assertEquals(3, info.get(0).getQuestionsInArrived());
        assertEquals(1, info.get(0).getQuestionsInRetrieved());
        assertEquals(2, info.get(1).getQuestionsInArrived());
    }
}
