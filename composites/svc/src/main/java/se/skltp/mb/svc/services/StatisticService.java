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
package se.skltp.mb.svc.services;

import java.util.List;

import se.skltp.mb.types.entity.MessageMeta;
import se.skltp.mb.types.entity.Statistic;

/**
 * Stores daily usage statistics.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public interface StatisticService {

    /**
     * Add the given messages as deliveries to the statistics tables.
     *
     * @param targetSystem   the target system which has taken delivery
     * @param deliveryTimeMs timestamp used
     * @param messages       all messages delivered
     */
    public void addDeliveriesToStatistics(String targetSystem, long deliveryTimeMs, List<MessageMeta> messages);

    /**
     * Get statistics for the days which contains the given start/end times.
     *
     * @param start start time (will be truncated to canonical day)
     * @param end   end time (will be truncated to canonical day)
     * @return stats for the slice of days
     */
    List<Statistic> getStatisticsForTimeSlice(long start, long end);
}
