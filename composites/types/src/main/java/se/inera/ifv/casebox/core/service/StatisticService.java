package se.inera.ifv.casebox.core.service;

import java.util.Collection;

import se.inera.ifv.casebox.core.entity.MessageType;
import se.inera.ifv.casebox.core.service.impl.StatisticInfo;

/**
 * Statistic service interface to store statics about number of handled answers and questions in the system. 
 */
public interface StatisticService {

    /**
     * Returns a list with statistic for last month per each careunit. 
     * @return
     */
    Collection<StatisticInfo> findStatisticLastMonth();

    /**
     * Stores the statistics for the given careunit and messagetype. By default it is stored for the current day.
     * @param careUnit  the careunit to store the statistics
     * @param number    the number of deleted message types
     * @param messageType the messagetype, answer or question
     */
    void storeStatistics(String careUnit, int number, MessageType messageType);
}
