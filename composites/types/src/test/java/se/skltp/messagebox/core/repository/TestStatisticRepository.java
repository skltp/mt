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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestStatisticRepository extends JpaRepositoryTestBase {
    private static long MS_DAY = 24 * 3600 * 1000;

    @Autowired
    private StatisticRepository statisticRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testPersist() throws Exception {
        long time = System.currentTimeMillis();

        Map<String, Integer> deliveriesPerServiceContracts = new HashMap<>();
        deliveriesPerServiceContracts.put("sc1", 1);
        deliveriesPerServiceContracts.put("sc2", 2);

        statisticRepository.addDeliveries("rec1", time, deliveriesPerServiceContracts);

        entityManager.flush();
        entityManager.clear();

        List<Statistic> statistics = statisticRepository.getStatistics(time-MS_DAY, time);

        Statistic sc1 = getStats(statistics, "rec1", time, "sc1");
        Statistic sc2 = getStats(statistics, "rec1", time, "sc2");

        assertEquals(sc1.getServiceContract(), "sc1");
        assertEquals(sc2.getServiceContract(), "sc2");
        assertEquals(1, sc1.getDeliveryCount());
        assertEquals(2, sc2.getDeliveryCount());

    }



    @Test
    public void testComplexOneDay() throws Exception {
        long time = System.currentTimeMillis();

        Map<String, Integer> deliveriesPerServiceContracts = new HashMap<>();
        deliveriesPerServiceContracts.put("sc1", 1);
        deliveriesPerServiceContracts.put("sc2", 2);

        statisticRepository.addDeliveries("rec1", time, deliveriesPerServiceContracts);

        deliveriesPerServiceContracts.clear();
        deliveriesPerServiceContracts.put("sc2", 1);
        deliveriesPerServiceContracts.put("sc3", 2);

        statisticRepository.addDeliveries("rec1", time, deliveriesPerServiceContracts);

        deliveriesPerServiceContracts.clear();
        deliveriesPerServiceContracts.put("sc1", 3);
        deliveriesPerServiceContracts.put("sc2", 4);
        deliveriesPerServiceContracts.put("sc3", 6);

        statisticRepository.addDeliveries("rec1", time, deliveriesPerServiceContracts);

        List<Statistic> statistics = statisticRepository.getStatistics(time - MS_DAY, time);

        Statistic sc1 = getStats(statistics, "rec1", time, "sc1");
        Statistic sc2 = getStats(statistics, "rec1", time, "sc2");
        Statistic sc3 = getStats(statistics, "rec1", time, "sc3");

        assertEquals(4, sc1.getDeliveryCount());
        assertEquals(7, sc2.getDeliveryCount());
        assertEquals(8, sc3.getDeliveryCount());
    }

    @Test
    public void testMonthly() throws Exception {
        long time = System.currentTimeMillis();

        Map<String, Integer> deliveriesPerServiceContracts = new HashMap<>();

        deliveriesPerServiceContracts.clear();
        deliveriesPerServiceContracts.put("sc1", 1);
        deliveriesPerServiceContracts.put("sc2", 2);
        deliveriesPerServiceContracts.put("sc3", 3);

        for (int i = 0; i < 20; i++) {
            statisticRepository.addDeliveries("rec1", time - MS_DAY * i, deliveriesPerServiceContracts);
        }

        List<Statistic> statistics = statisticRepository.getStatistics(time - 30 * MS_DAY, time);
        assertEquals(60, statistics.size());
    }


    private Statistic getStats(List<Statistic> statistics, String receiverId, long time, String serviceContract) {
        long canonicalDayTime = Statistic.convertToCanonicalDayTime(time);

        for ( Statistic stat : statistics ) {
            if (stat.getCanonicalDayTime() == canonicalDayTime
                    && stat.getReceiverId().equals(receiverId)
                    && stat.getServiceContract().equals(serviceContract)  ) {
                return stat;
            }
        }
        return null;
    }

}
