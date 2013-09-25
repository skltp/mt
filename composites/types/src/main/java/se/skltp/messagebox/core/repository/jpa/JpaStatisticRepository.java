package se.skltp.messagebox.core.repository.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
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
    public void addDeliveries(String receiverId, long time, Map<String, Integer> deliveriesPerServiceContracts) {

        long canonicalDayTime = Statistic.convertToCanonicalDayTime(time);

        // prepare the existing stats into a per-serviceContract map
        Map<String, Statistic> existingMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        List<Statistic> statsForDayAndReceiver = entityManager.createNamedQuery("Statistic.getForReceiverAndDayTime")
                .setParameter("receiverId", receiverId)
                .setParameter("time", canonicalDayTime)
                .getResultList();

        for ( Statistic statistic : statsForDayAndReceiver ) {
            Statistic stat = existingMap.put(statistic.getServiceContract(), statistic);
            assert stat == null;
        }

        // add deliveryCount to either existing or new stats
        for ( Map.Entry<String, Integer> entry : deliveriesPerServiceContracts.entrySet() ) {
            String serviceContract = entry.getKey();
            int count = entry.getValue();
            Statistic stat = existingMap.get(serviceContract);
            if (stat == null) {
                stat = new Statistic(receiverId,serviceContract, canonicalDayTime);
                entityManager.persist(stat);
            }
            stat.addDeliveries(count);
        }
    }

    @Override
    public List<Statistic> getStatistics(long startTime, long endTime) {
        long canonicalDayStartTime = Statistic.convertToCanonicalDayTime(startTime);

        //noinspection unchecked
        return entityManager.createNamedQuery("Statistic.getForTimeSlice")
                .setParameter("startTime", canonicalDayStartTime)
                .setParameter("endTime", endTime)
                .getResultList();

    }
}
