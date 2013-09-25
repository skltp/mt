package se.skltp.messagebox.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void addDeliveriesToStatistics(String receiverId, long time, List<Message> messages) {

        Map<String,Integer> deliveriesPerServiceContractMap = new HashMap<>();
        for ( Message msg : messages ) {
            Integer value = deliveriesPerServiceContractMap.get(msg.getServiceContract());
            int v = value == null ? 0 : value;
            deliveriesPerServiceContractMap.put(msg.getServiceContract(), ++v);
        }

        statisticRepository.addDeliveries(receiverId, time, deliveriesPerServiceContractMap);
    }

    @Override
    public List<Statistic> getStatisticsForDay(long timestamp) {
        long dayTime = Statistic.convertToCanonicalDayTime(timestamp);
        return statisticRepository.getStatistics(dayTime, dayTime);
    }
}
