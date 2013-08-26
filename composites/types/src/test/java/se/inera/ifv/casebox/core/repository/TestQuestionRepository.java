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
package se.inera.ifv.casebox.core.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.ifv.casebox.core.entity.Question;
import se.inera.ifv.casebox.util.JpaRepositoryTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestQuestionRepository extends JpaRepositoryTestBase {

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testPersist() throws Exception {
        Question question = new Question("bepa", "Some serializable object");
        questionRepository.persist(question);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM QUESTION"));
    }

    @Test
    public void testFindByCareUnit() throws Exception {
        Question question = new Question("bepa", "Some serializable object");
        questionRepository.persist(question);

        entityManager.flush();
        entityManager.clear();

        List<Question> questions = questionRepository.findForCareUnit("bepa", 10);

        assertEquals(1, questions.size());
    }

    @Test
    public void testOrder() throws Exception {
        for(int i = 0 ; i < 5 ; i++) {
            entityManager.persist(new Question("careUnit1","Some content"));
        }
        
        entityManager.flush();
        entityManager.clear();
        
        List<Question> questions = questionRepository.findForCareUnit("careUnit1", 5);
        
        int first = questions.get(0).getId().intValue();
        assertEquals(first + 1, questions.get(1).getId().intValue());
        assertEquals(first + 2, questions.get(2).getId().intValue());
        assertEquals(first + 3, questions.get(3).getId().intValue());
        assertEquals(first + 4, questions.get(4).getId().intValue());
    }

    @Test
    public void testGetNumOfQuestions() throws Exception {
        questionRepository.persist(new Question("bepa", "Some serializable object"));
        questionRepository.persist(new Question("bepa", "Some serializable object"));
        questionRepository.persist(new Question("bepa", "Some serializable object"));
        questionRepository.persist(new Question("apa", "Some serializable object"));
        questionRepository.persist(new Question("apa", "Some serializable object"));

        entityManager.flush();
        entityManager.clear();
        
        long cnt = questionRepository.getNumOfQuestionsForCareUnit("bepa");
        
        assertEquals(3, cnt);
        
    }

    @Test
    public void testDelete() throws Exception {
        Question question = new Question("bepa", "Some serializable object");
        questionRepository.persist(question);

        entityManager.flush();
        entityManager.clear();

        questionRepository.remove(question.getId());

        entityManager.flush();

        assertEquals(0, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM QUESTION"));
    }
}
