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
package se.skltp.messagebox.core.repository;

import java.util.*;
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
import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:services-config.xml"})
public class TestStatisticRepository extends JpaRepositoryTestBase {
    private static long MS_DAY = 24 * 3600 * 1000;

    @Autowired
    private StatisticRepository statisticRepository;

    @PersistenceContext
    EntityManager entityManager;

    private long deliveryTime;

    @Override
    public void onSetup2() {
        deliveryTime = System.currentTimeMillis();
    }

    @Test
    public void testPersist() throws Exception {
        List<Message> messages = new ArrayList<>();
        messages.add(createMsg("rec1", "sc1", 100));
        messages.add(createMsg("rec1", "sc2", 200));
        messages.add(createMsg("rec1", "sc2", 300));

        statisticRepository.addDeliveries("rec1", deliveryTime, messages);

        entityManager.flush();
        entityManager.clear();

        List<Statistic> statistics = statisticRepository.getStatistics(deliveryTime, deliveryTime);

        Statistic sc1 = getStats(statistics, "rec1", deliveryTime, "sc1");
        Statistic sc2 = getStats(statistics, "rec1", deliveryTime, "sc2");

        assertEquals(sc1.getServiceContract(), "sc1");
        assertEquals(1, sc1.getDeliveryCount());
        assertEquals(100, sc1.getTotalWaitTimeMs());
        assertEquals(100, sc1.getMaxWaitTimeMs());

        assertEquals(sc2.getServiceContract(), "sc2");
        assertEquals(2, sc2.getDeliveryCount());
        assertEquals(300, sc2.getMaxWaitTimeMs());
        assertEquals(500, sc2.getTotalWaitTimeMs());
    }


    @Test
    public void testComplexOneDay() throws Exception {
        List<Message> messages = new ArrayList<>();
        messages.add(createMsg("rec1", "sc1", 100));
        messages.add(createMsg("rec1", "sc2", 200));
        messages.add(createMsg("rec1", "sc2", 300));

        statisticRepository.addDeliveries("rec1", deliveryTime, messages);

        messages.clear();
        messages.add(createMsg("rec1", "sc2", 300));
        messages.add(createMsg("rec1", "sc3", 50));

        statisticRepository.addDeliveries("rec1", deliveryTime, messages);

        messages.clear();
        messages.add(createMsg("rec1", "sc1", 100));
        messages.add(createMsg("rec1", "sc1", 100));
        messages.add(createMsg("rec1", "sc2", 400));
        messages.add(createMsg("rec1", "sc3", 600));

        statisticRepository.addDeliveries("rec1", deliveryTime, messages);

        List<Statistic> statistics = statisticRepository.getStatistics(deliveryTime, deliveryTime);

        Statistic sc1 = getStats(statistics, "rec1", deliveryTime, "sc1");
        Statistic sc2 = getStats(statistics, "rec1", deliveryTime, "sc2");
        Statistic sc3 = getStats(statistics, "rec1", deliveryTime, "sc3");

        assertEquals(3, sc1.getDeliveryCount());
        assertEquals(4, sc2.getDeliveryCount());
        assertEquals(2, sc3.getDeliveryCount());
    }

    @Test
    public void testMonthly() throws Exception {

        List<Message> messages = new ArrayList<>();

        for ( int i = 0; i < 20; i++ ) {
            long deliTime = deliveryTime - MS_DAY * i;
            messages.add(new Message("rec1", "targetOrg", "sk1", "body", MessageStatusType.RECEIVED, new Date(deliTime - 100)));
            statisticRepository.addDeliveries("rec1", deliTime, messages);
        }

        List<Statistic> statistics = statisticRepository.getStatistics(deliveryTime - 30 * MS_DAY, deliveryTime);
        assertEquals(20, statistics.size());
    }


    private Statistic getStats(List<Statistic> statistics, String receiverId, long time, String serviceContract) {
        long canonicalDayTime = Statistic.convertToCanonicalDayTime(time);

        for ( Statistic stat : statistics ) {
            if ( stat.getCanonicalDayTime() == canonicalDayTime
                    && stat.getReceiverId().equals(receiverId)
                    && stat.getServiceContract().equals(serviceContract) ) {
                return stat;
            }
        }
        return null;
    }

    private Message createMsg(String receiverId, String serviceContract, long deltaTime) {
        return new Message(receiverId, "targetOrg", serviceContract, "body", MessageStatusType.RECEIVED, new Date(deliveryTime - deltaTime));
    }


}
