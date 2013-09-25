package se.skltp.messagebox.core.repository;

import java.util.List;
import java.util.Map;

import se.skltp.messagebox.core.entity.Statistic;
import se.vgregion.dao.domain.patterns.repository.Repository;

/**
 * @author mats.olsson@callistaenterprise.se
 */
public interface StatisticRepository extends Repository<Statistic, Long> {

    /**
     * Return a map of all the stats for the given receiver and day.
     *
     *
     * @param receiverId the receiver for all the messages
     * @param time time of delivery
     * @param deliveriesPerServiceContracts number of deliveries for each service contract
     */
     void addDeliveries(String receiverId, long time, Map<String, Integer> deliveriesPerServiceContracts);

    /**
     * Get the statistic for the given timeslice.
     *
     * @param startTime specifies the starting time to collect statistics for (will be converted to canonical time)
     * @param endTime specifies the end day to get stats for
     * @return list of all stats entries for the given time range
     */
    List<Statistic> getStatistics(long startTime, long endTime);
}
