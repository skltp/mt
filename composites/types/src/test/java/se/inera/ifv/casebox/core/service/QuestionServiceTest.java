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

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.ifv.casebox.core.entity.Question;
import se.inera.ifv.casebox.util.JpaRepositoryTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionServiceTest extends JpaRepositoryTestBase {

    @Autowired
    QuestionService questionService;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testFind() throws Exception {
        entityManager.persist(new Question("careUnit1", "Some serializable1"));
        entityManager.persist(new Question("careUnit2", "Some serializable2"));
        entityManager.persist(new Question("careUnit2", "Some serializable3"));

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, questionService.getQuestionsForCareUnit("careUnit1").getQuestions().size());
        assertEquals(2, questionService.getQuestionsForCareUnit("careUnit2").getQuestions().size());
        assertEquals(0, questionService.getQuestionsForCareUnit("careUnit1").getQuestionsLeft());
    }

    @Test
    public void testFindMoreThanMax() throws Exception {
        for (int i = 0; i < 9; i++) {
            entityManager.persist(new Question("careUnit1", "Some serializable1"));
        }

        entityManager.flush();
        entityManager.clear();

        assertEquals(5, questionService.getQuestionsForCareUnit("careUnit1").getQuestions().size());
        assertEquals(4, questionService.getQuestionsForCareUnit("careUnit1").getQuestionsLeft());
    }

    @Test
    public void testFindAndChangeStatus() throws Exception {
        for (int i = 0; i < 4; i++) {
            entityManager.persist(new Question("careUnit1", "Some serializable1"));
        }

        entityManager.flush();
        entityManager.clear();

        assertEquals(0, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM QUESTION WHERE STATUS='RETRIEVED'"));

        assertEquals(4, questionService.getQuestionsForCareUnit("careUnit1").getQuestions().size());

        entityManager.flush();
        entityManager.clear();

        assertEquals(4, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM QUESTION WHERE STATUS='RETRIEVED'"));
    }

    @Test
    public void testDelete() throws Exception {
        Question[] questions = new Question[5];
        questions[0] = new Question("careUnit1", "Some Serializable1");
        questions[1] = new Question("careUnit1", "Some Serializable2");
        questions[2] = new Question("careUnit1", "Some Serializable3");
        questions[3] = new Question("careUnit1", "Some Serializable4");
        questions[4] = new Question("careUnit2", "Some Serializable5");
        Set<Long> ids = new HashSet<Long>();
        for (Question q : questions) {
            entityManager.persist(q);
            q.setStatusRetrieved();
            if (q.getCareUnit().equals("careUnit1")) {
                ids.add(q.getId());
            }
        }

        entityManager.flush();
        entityManager.clear();

        assertEquals(5, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM QUESTION"));

        questionService.deleteQuestionsForCareUnit("careUnit1", ids);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM QUESTION"));

        ITable result = getConnection().createQueryTable("STATISTIC",
                "SELECT * FROM STATISTIC WHERE CARE_UNIT = 'careUnit1'");
        Assert.assertEquals(1, result.getRowCount());
        Assert.assertEquals("Question", result.getValue(0, "MESSAGE_TYPE"));
        Assert.assertEquals(4, result.getValue(0, "NUMBER"));
    }
}
