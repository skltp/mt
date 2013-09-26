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
     * @param receiverId     the receiver
     * @param deliveryTimeMs timestamp used
     * @param messages       all messages delivered
     */
    public void addDeliveriesToStatistics(String receiverId, long deliveryTimeMs, List<Message> messages);

    /**
     * Get statistics for the day of the timestamp.
     *
     * @param timestamp used to find canonicalDayTime
     * @return stats for the day
     */
    List<Statistic> getStatisticsForDay(long timestamp);
}
