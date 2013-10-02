package se.skltp.messagebox.core.service;

import java.util.List;

import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.Statistic;

/**
 * @author mats.olsson@callistaenterprise.se
 */
public interface StatisticService {

    /**
     * Add the given messages as deliveries to the statistics tables.
     *
     * @param targetSystem     the receiver
     * @param deliveryTimeMs timestamp used
     * @param messages       all messages delivered
     */
    public void addDeliveriesToStatistics(String targetSystem, long deliveryTimeMs, List<Message> messages);

    /**
     * Get statistics for the days which contains the given start/end times.
     *
     * @param start start time
     * @param end end time
     * @return stats for the slice of days
     */
    List<Statistic> getStatisticsForTimeSlice(long start, long end);
}
