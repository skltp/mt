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
package se.skltp.messagebox.core.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import se.skltp.messagebox.core.entity.MessageMeta;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.repository.StatisticRepository;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

/**
 * Access to Statistic entities.
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Repository
public class JpaStatisticRepository extends DefaultJpaRepository<Statistic, Long> implements StatisticRepository {



    @Override
    public void addDeliveries(String targetSystem, long deliveryTime, List<MessageMeta> messages) {

        long canonicalDayTime = Statistic.convertToCanonicalDayTime(deliveryTime);

        // Load up all the existing stats objects.
        List<Statistic> statsForDayAndTargetSystem = entityManager.createNamedQuery("Statistic.getForTargetSystemAndDay", Statistic.class)
                .setParameter("targetSystem", targetSystem)
                .setParameter("time", canonicalDayTime)
                .getResultList();

        Map<Key, Statistic> statisticMap = new HashMap<Key, Statistic>();
        for ( Statistic statistic : statsForDayAndTargetSystem ) {
            Statistic stat = statisticMap.put(new Key(statistic), statistic);
            assert stat == null;
        }

        for ( MessageMeta msg : messages ) {
            assert targetSystem.equals(msg.getTargetSystem());
            Key key = new Key(msg);
            Statistic stat = statisticMap.get(key);
            if (stat == null) {
                stat = new Statistic(targetSystem, msg.getTargetOrganization(), msg.getServiceContract(), canonicalDayTime);
                entityManager.persist(stat);
                statisticMap.put(key, stat);
            }
            stat.addDelivery(deliveryTime - msg.getArrived().getTime());
        }
    }

    @Override
    public List<Statistic> getStatistics(long startTime, long endTime) {
        long canonicalDayStartTime = Statistic.convertToCanonicalDayTime(startTime);

        return entityManager.createNamedQuery("Statistic.getForTimeSlice", Statistic.class)
                .setParameter("startTime", canonicalDayStartTime)
                .setParameter("endTime", endTime)
                .getResultList();

    }

    /**
     * Key for finding out which statistic a message should use
     */
    private static class Key {
        private String targetOrg;
        private String serviceContract;

         Key(MessageMeta msg) {
            this.targetOrg = msg.getTargetOrganization();
            this.serviceContract = msg.getServiceContract();
        }

         Key(Statistic stat) {
             this.targetOrg = stat.getTargetOrganization();
             this.serviceContract = stat.getServiceContract();
         }

        @Override
        public boolean equals(Object o) {
            if ( this == o ) return true;
            if ( o == null || getClass() != o.getClass() ) return false;

            Key key = (Key) o;

            if ( serviceContract != null ? !serviceContract.equals(key.serviceContract) : key.serviceContract != null )
                return false;
            if ( targetOrg != null ? !targetOrg.equals(key.targetOrg) : key.targetOrg != null ) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = targetOrg != null ? targetOrg.hashCode() : 0;
            result = 31 * result + (serviceContract != null ? serviceContract.hashCode() : 0);
            return result;
        }
    }
}
