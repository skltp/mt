package se.inera.ifv.casebox.core.repository;

import java.util.Date;

import se.inera.ifv.casebox.core.entity.MessageType;
import se.inera.ifv.casebox.core.entity.Statistic;
import se.vgregion.dao.domain.patterns.repository.Repository;

/**
 * Repository for handling statistic in the system. 
 */
public interface StatisticRepository extends Repository<Statistic, Long> {

    /**
     * Returns the statistics for the given messagetype, careunit and the logged date. 
     * @param messageType       Question or Answer message type
     * @param careUnit          The care unit
     * @param removed           The date the statitics was created.
     * @return
     */
    Statistic find(MessageType messageType, String careUnit, Date created);
}
