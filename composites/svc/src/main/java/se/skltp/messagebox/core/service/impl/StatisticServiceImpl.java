package se.skltp.messagebox.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.Statistic;
import se.skltp.messagebox.core.repository.StatisticRepository;
import se.skltp.messagebox.core.service.StatisticService;

/**
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;


    @Override
    public void addDeliveriesToStatistics(String receiverId, long deliveryTimeMs, List<Message> messages) {
        statisticRepository.addDeliveries(receiverId, deliveryTimeMs, messages);
    }

    @Override
    public List<Statistic> getStatisticsFor30Days(long timestamp) {
        long thirtyDays = 30 * 24 * 3600 * 1000L;
        return statisticRepository.getStatistics(timestamp - thirtyDays, timestamp);
    }

}
