package se.skltp.mt.core.service.impl;

import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.skltp.mt.core.entity.MessageType;
import se.skltp.mt.core.entity.Statistic;
import se.skltp.mt.core.repository.StatisticRepository;
import se.skltp.mt.core.service.StatisticService;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;

    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Collection<StatisticInfo> findStatisticLastMonth() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);

        // search on 30 days back in time
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 30);
        try {
            return entityManager.createQuery(
                    "select " + 
                    "new se.skltp.mt.core.service.impl.StatisticInfo(s.careUnit, sum(s.number)) " +
                    "from Statistic s where s.created > :created group by s.careUnit")
                    .setParameter("created", cal.getTime()).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void storeStatistics(String careUnit, int numberOfMessages, MessageType messageType) {
        // Store statistic for the deleted objects
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        Statistic statistic = statisticRepository.find(messageType, careUnit, cal.getTime());
        if (statistic == null) {
            statistic = new Statistic(messageType, cal.getTime(), careUnit, numberOfMessages);
            statisticRepository.store(statistic);
        } else {
            statistic.setNumber(statistic.getNumber() + numberOfMessages);
            statisticRepository.merge(statistic);
        }
    }
}
