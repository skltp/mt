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
package se.skltp.messagebox.svc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.types.entity.MessageMeta;
import se.skltp.messagebox.types.entity.Statistic;
import se.skltp.messagebox.types.repository.StatisticRepository;
import se.skltp.messagebox.svc.services.StatisticService;

/**
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;


    @Override
    public void addDeliveriesToStatistics(String targetSystem, long deliveryTimeMs, List<MessageMeta> messages) {
        statisticRepository.addDeliveries(targetSystem, deliveryTimeMs, messages);
    }

    @Override
    public List<Statistic> getStatisticsForTimeSlice(long startOfSlice, long endOfSlice) {
        return statisticRepository.getStatistics(startOfSlice, endOfSlice);
    }

}
