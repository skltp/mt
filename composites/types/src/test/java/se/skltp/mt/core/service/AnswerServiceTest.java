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
package se.skltp.mt.core.service;

import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.skltp.mt.core.entity.Answer;
import se.skltp.mt.core.service.AnswerService;
import se.skltp.mt.util.JpaRepositoryTestBase;

@RunWith(SpringJUnit4ClassRunner.class)
public class AnswerServiceTest extends JpaRepositoryTestBase {

    @Autowired
    private AnswerService answerService;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void deleteAnswers() throws Exception {
        Answer answer = new Answer("careUnit", "Some serializable object");
        answerService.saveAnswer(answer);

        entityManager.flush();

        Set<Long> ids = new HashSet<Long>();
        ids.add(answer.getId());
        try {
            answerService.deleteAnswersForCareUnit("careUnit", ids);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // TODO: How can we check that the transaction is marked for rolled back?
    }

    @Ignore("uses non-transactional getConnection()") @Test
    public void checkStatistics() throws Exception {
        Answer answer = new Answer("careUnit", "Some serializable object");
        answer.setStatusRetrieved();
        answerService.saveAnswer(answer);

        Set<Long> ids = new HashSet<Long>();
        ids.add(answer.getId());
        answerService.deleteAnswersForCareUnit("careUnit", ids);

        entityManager.flush();

        ITable result = getConnection().createQueryTable("STATISTIC",
                "SELECT * FROM STATISTIC WHERE CARE_UNIT = 'careUnit'");
        Assert.assertEquals(1, result.getRowCount());
        Assert.assertEquals("Answer", result.getValue(0, "MESSAGE_TYPE"));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        Assert.assertEquals(cal.getTime(), result.getValue(0, "CREATED"));
    }

    @Ignore("uses non-transactional getConnection()") @Test
    public void createStatisticsForSameDay() throws Exception {
        
        // Create first answer and delete it
        Answer answer = new Answer("careUnit", "Some serializable object");
        answer.setStatusRetrieved();
        answerService.saveAnswer(answer);

        Set<Long> ids = new HashSet<Long>();
        ids.add(answer.getId());

        // delete answers
        answerService.deleteAnswersForCareUnit("careUnit", ids);
        entityManager.flush();
        
        // create second answer and delete it
        answer = new Answer("careUnit", "Some serializable object");
        answer.setStatusRetrieved();
        answerService.saveAnswer(answer);
        
        ids = new HashSet<Long>();
        ids.add(answer.getId());
        
        answerService.deleteAnswersForCareUnit("careUnit", ids);
        entityManager.flush();

        // Check so that it only exist one statistics row for this careunit and day
        ITable result = getConnection().createQueryTable("STATISTIC",
                "SELECT * FROM STATISTIC WHERE CARE_UNIT = 'careUnit'");
        Assert.assertEquals(1, result.getRowCount());
        Assert.assertEquals(2, result.getValue(0, "NUMBER"));
    }
}
