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
package se.skltp.messagebox.core.service;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:services-config.xml"})
public class TestStatisticService extends JpaRepositoryTestBase {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    StatisticService statisticService;

    @Autowired
    MessageService messageService;

    @Test
    public void testDeliverOneMessage() throws Exception {
        String receiverId = "recId";
        String serviceContract = "sc1";
        Message message = new Message(receiverId, "targetOrg", serviceContract, "messageBody");
        messageService.saveMessage(message);

        message.setStatusRetrieved(); // allow the messaged to be deleted

        entityManager.flush();
        entityManager.clear();

        long timestamp = System.currentTimeMillis();
        List<Statistic> stats = statisticService.getStatisticsForDay(timestamp);
        assertEquals(0, stats.size());

        messageService.deleteMessages(receiverId, timestamp, Collections.singletonList(message));

        stats = statisticService.getStatisticsForDay(timestamp);
        assertEquals(1, stats.size());
        Statistic s = stats.get(0);
        assertEquals(receiverId, s.getReceiverId());
        assertEquals(serviceContract, s.getServiceContract());
        assertEquals(1, s.getDeliveryCount());
    }

    @Test
    public void testDeliverTwoMessages() throws Exception {
        String receiverId = "recId";
        String serviceContract1 = "sc1";
        String serviceContract2 = "sc2";
        Message msg1 = new Message(receiverId, "targetOrg", serviceContract1, "messageBody");
        Message msg2 = new Message(receiverId, "targetOrg", serviceContract2, "messageBody");
        messageService.saveMessage(msg1);
        messageService.saveMessage(msg2);

        msg1.setStatusRetrieved(); // allow the messaged to be deleted
        msg2.setStatusRetrieved(); // allow the messaged to be deleted

        entityManager.flush();
        entityManager.clear();

        long timestamp = System.currentTimeMillis();
        long timestampTooLongAgo = System.currentTimeMillis() - 2 * 24 * 3600 * 1000;
        List<Statistic> stats = statisticService.getStatisticsForDay(timestamp);
        assertEquals(0, stats.size());

        messageService.deleteMessages(receiverId, timestampTooLongAgo, Collections.singletonList(msg1));
        messageService.deleteMessages(receiverId, timestamp, Collections.singletonList(msg2));

        stats = statisticService.getStatisticsForDay(timestamp);
        assertEquals(1, stats.size());
        Statistic s = stats.get(0);
        assertEquals(receiverId, s.getReceiverId());
        assertEquals(serviceContract2, s.getServiceContract());
        assertEquals(1, s.getDeliveryCount());

        stats = statisticService.getStatisticsForDay(timestampTooLongAgo);
        assertEquals(1, stats.size());
        s = stats.get(0);
        assertEquals(receiverId, s.getReceiverId());
        assertEquals(serviceContract1, s.getServiceContract());
        assertEquals(1, s.getDeliveryCount());

    }
}
