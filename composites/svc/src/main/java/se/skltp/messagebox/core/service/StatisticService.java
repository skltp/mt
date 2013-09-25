package se.skltp.messagebox.core.service;

import java.util.List;

import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.Statistic;

/**
 * @author mats.olsson@callistaenterprise.se
 */
public interface StatisticService {

    /**
     * Add the given messages as deliveres to the statistics tables.
     *
     * @param receiverId the reciver
     * @param time       timestamp used
     * @param messages   all messages delivered
     */
    public void addDeliveriesToStatistics(String receiverId, long time, List<Message> messages);

    List<Statistic> getStatisticsForDay(long timestamp);
}
