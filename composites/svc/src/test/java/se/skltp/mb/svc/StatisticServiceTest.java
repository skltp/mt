/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera MessageService (http://code.google.com/p/inera-message).
 *
 * Inera MessageService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera MessageService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.skltp.mb.svc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.mb.types.services.TimeService;
import se.skltp.mb.svc.services.MessageService;
import se.skltp.mb.svc.services.StatisticService;
import se.skltp.mb.types.entity.MessageBody;
import se.skltp.mb.types.entity.MessageMeta;
import se.skltp.mb.types.entity.MessageStatus;
import se.skltp.mb.types.entity.Statistic;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:services-config.xml"})
public class StatisticServiceTest extends JpaRepositoryTestBase {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    StatisticService statisticService;

    @Autowired
    TimeService timeService;

    @Autowired
    MessageService messageService;
    private long thirtyDays = 30 * 24 * 3600 * 1000L;
    private String correlationId = "correlationId";

    @Test
    public void testDeliverOneMessage() throws Exception {
        String targetSystem = "recId";
        String serviceContract = "sc1";
        MessageMeta message = messageService.create("sourceId", targetSystem, "targetOrg", serviceContract, "messageBody", correlationId);

        message.setStatusRetrieved(); // allow the messaged to be deleted

        entityManager.flush();
        entityManager.clear();

        long timestamp = timeService.now();

        List<Statistic> stats = statisticService.getStatisticsForTimeSlice(timestamp - thirtyDays, timestamp);
        assertEquals(0, stats.size());

        messageService.deleteMessages(targetSystem, timestamp, Collections.singletonList(message));

        stats = statisticService.getStatisticsForTimeSlice(timestamp - thirtyDays, timestamp);
        assertEquals(1, stats.size());
        Statistic s = stats.get(0);
        assertEquals(targetSystem, s.getTargetSystem());
        assertEquals(serviceContract, s.getServiceContract());
        assertEquals(1, s.getDeliveryCount());
    }

    @Test
    public void testDeliverTwoMessages() throws Exception {
        Date now = new Date(timeService.now());
        String targetSystem = "recId";
        String serviceContract1 = "sc1";
        String serviceContract2 = "sc2";
        MessageMeta msg1 = createMsg(now, targetSystem, serviceContract1);
        MessageMeta msg2 = createMsg(now, targetSystem, serviceContract2);

        entityManager.flush();
        entityManager.clear();

        long timestampTooLongAgo = now.getTime();
        long fourtyDays = 40 * 24 * 3600 * 1000L;
        timestampTooLongAgo -= fourtyDays;
        List<Statistic> stats = statisticService.getStatisticsForTimeSlice(now.getTime() - thirtyDays, now.getTime());
        assertEquals(0, stats.size());

        messageService.deleteMessages(targetSystem, timestampTooLongAgo, Collections.singletonList(msg1));
        messageService.deleteMessages(targetSystem, now.getTime(), Collections.singletonList(msg2));

        stats = statisticService.getStatisticsForTimeSlice(now.getTime() - thirtyDays, now.getTime());
        assertEquals(1, stats.size());
        Statistic s = stats.get(0);
        assertEquals(targetSystem, s.getTargetSystem());
        assertEquals(serviceContract2, s.getServiceContract());
        assertEquals(1, s.getDeliveryCount());

        stats = statisticService.getStatisticsForTimeSlice(timestampTooLongAgo - thirtyDays, timestampTooLongAgo);
        assertEquals(1, stats.size());
        s = stats.get(0);
        assertEquals(targetSystem, s.getTargetSystem());
        assertEquals(serviceContract1, s.getServiceContract());
        assertEquals(1, s.getDeliveryCount());

    }

    private MessageMeta createMsg(Date now, String targetSystem, String serviceContract1) {
        MessageBody body = new MessageBody("body");
        MessageMeta messageMeta = new MessageMeta("sourceId", targetSystem, "targetOrg1", serviceContract1, body, correlationId, MessageStatus.RETRIEVED, now);
        messageService.saveMessage(messageMeta);
        return messageMeta;
    }

    @Test
    public void testDeliverToTwoTargetOrgs() throws Exception {
        Date now = new Date(timeService.now());
        String targetSystem = "recId";
        String serviceContract1 = "sc1";
        String serviceContract2 = "sc2";
        String targetOrg1 = "targetOrg1";
        String targetOrg2 = "targetOrg2";

        MessageMeta msg1 = new MessageMeta("sourceId", targetSystem, targetOrg1, serviceContract1, new MessageBody("txt"), correlationId, MessageStatus.RETRIEVED, now);
        MessageMeta msg2 = new MessageMeta("sourceId", targetSystem, targetOrg2, serviceContract2, new MessageBody("txt"), correlationId, MessageStatus.RETRIEVED, now);
        messageService.saveMessage(msg1);
        messageService.saveMessage(msg2);

        entityManager.flush();
        entityManager.clear();

        List<Statistic> stats = statisticService.getStatisticsForTimeSlice(now.getTime() - thirtyDays, now.getTime());
        assertEquals(0, stats.size());

        messageService.deleteMessages(targetSystem, now.getTime(), Arrays.asList(msg1, msg2));

        stats = statisticService.getStatisticsForTimeSlice(now.getTime() - thirtyDays, now.getTime());
        assertEquals(2, stats.size());
        Statistic s1 = stats.get(0);
        Statistic s2 = stats.get(1);
        if (s1.getServiceContract().equals(serviceContract2)) {
            Statistic tmp = s1;
            s1 = s2;
            s2 = tmp;
        }

        assertEquals(targetSystem, s1.getTargetSystem());
        assertEquals(targetOrg1, s1.getTargetOrganization());
        assertEquals(serviceContract1, s1.getServiceContract());
        assertEquals(1, s1.getDeliveryCount());

        assertEquals(targetSystem, s2.getTargetSystem());
        assertEquals(targetOrg2, s2.getTargetOrganization());
        assertEquals(serviceContract2, s2.getServiceContract());
        assertEquals(1, s2.getDeliveryCount());

    }
}
