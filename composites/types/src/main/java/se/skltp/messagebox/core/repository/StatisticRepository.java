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
package se.skltp.messagebox.core.repository;

import java.util.List;

import se.skltp.messagebox.core.entity.MessageMeta;
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
     * @return list of all stats entries for the given time range ordered by targetSystem, targetOrganization,
     * serviceContract and canonicalDayTime
     */
    List<Statistic> getStatistics(long startTime, long endTime);

    /**
     * Add statistics for delivering the given messages to the target system at deliveryTime.
     *
     * @param targetSystem all messages must have this as targetSystem
     * @param deliveryTime time to use as delivery time
     * @param messages messages to tote up stats for
     */
    void addDeliveries(String targetSystem, long deliveryTime, List<MessageMeta> messages);
}
