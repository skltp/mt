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
package se.skltp.mt.core.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import se.skltp.mt.core.entity.Answer;
import se.skltp.mt.core.repository.AnswerRepository;
import se.skltp.mt.util.JpaRepositoryTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestAnswerRepository extends JpaRepositoryTestBase {

    @Autowired
    private AnswerRepository answerRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void persist() throws Exception {
        String careUnit = "careUnitId";
		Answer answer = new Answer(careUnit, "Some serializable object");
		answerRepository.persist(answer);

		answerRepository.flush();
		
		Assert.assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM ANSWER"));

    }

    @Transactional
    public void createAnswer(String careUnit) {
	}

    @Test
    public void testFindMaxNumberOfAnswers() throws Exception {
        for(int i = 0 ; i < 9 ; i++) {
            entityManager.persist(new Answer("careUnit1","Some content"));
        }
        
        entityManager.flush();
        entityManager.clear();
        
        List<Answer> answers = answerRepository.findForCareUnit("careUnit1", 5);
        
        assertEquals(5, answers.size());
    }
   

    @Test
    public void testOrder() throws Exception {
        for(int i = 0 ; i < 5 ; i++) {
            entityManager.persist(new Answer("careUnit1","Some content"));
        }
        
        entityManager.flush();
        entityManager.clear();
        
        List<Answer> answers = answerRepository.findForCareUnit("careUnit1", 5);
        
        int first = answers.get(0).getId().intValue();
        assertEquals(first + 1, answers.get(1).getId().intValue());
        assertEquals(first + 2, answers.get(2).getId().intValue());
        assertEquals(first + 3, answers.get(3).getId().intValue());
        assertEquals(first + 4, answers.get(4).getId().intValue());
    }
    
    @Test
    public void findByCareUnit() throws Exception {
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getXmlDataSet("answer.xml"));

        Answer answer = new Answer("kalle", "Some serializable object");
        answerRepository.persist(answer);

        List<Answer> questions = answerRepository.findAllForCareUnit("kalle");
        Assert.assertEquals(1, questions.size());
    }

    @Test
    public void removeAnswer() throws Exception {
        Answer answer = new Answer("benny1", "Some serializable object");
        answer = answerRepository.persist(answer);
        answerRepository.remove(answer);

        entityManager.flush();

        ITable result = getConnection().createQueryTable("ANSWER", "SELECT * FROM ANSWER WHERE CARE_UNIT = 'benny1'");
        Assert.assertEquals(0, result.getRowCount());
    }
}
