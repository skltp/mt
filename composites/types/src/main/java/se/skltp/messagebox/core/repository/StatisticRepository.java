package se.skltp.messagebox.core.repository;

import java.util.List;

import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.Statistic;
import se.vgregion.dao.domain.patterns.repository.Repository;

/**
 * Repository interface for Statistics
 *
 * @author mats.olsson@callistaenterprise.se
 */
public interface StatisticRepository extends Repository<Statistic, Long> {

    /**
     * Get the statistic for the days contained in the given timeslice.
     *
     * @param startTime specifies the starting time to collect statistics for (will be converted to canonical time)
     * @param endTime specifies the end day to get stats for
     * @return list of all stats entries for the given time range
     */
    List<Statistic> getStatistics(long startTime, long endTime);

    /**
     * Add statistics for delivering the given messages to the receiver at deliveryTime.
     *
     * @param targetSystem all messages must have this receiver
     * @param deliveryTime time to use as delivery time
     * @param messages messages to tote up stats for
     */
    void addDeliveries(String targetSystem, long deliveryTime, List<Message> messages);
}
