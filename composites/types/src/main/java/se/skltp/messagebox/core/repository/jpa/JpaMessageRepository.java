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
package se.skltp.messagebox.core.repository.jpa;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.MessageStatus;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.skltp.messagebox.core.service.TimeService;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

@Repository
public class JpaMessageRepository extends DefaultJpaRepository<Message, Long> implements MessageRepository {

    @Autowired
    TimeService timeService;

    @SuppressWarnings("unchecked")
    public List<Message> getMessages(String systemId, Collection<Long> ids) {
        if (ids.isEmpty()) {
            // you get an Illegal SQL statement if the set of ids to get is empty
            return Collections.emptyList();
        }
        return entityManager.createNamedQuery("Message.getMessages")
                .setParameter("targetSystem", systemId)
                .setParameter("ids", ids)
                .getResultList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Message> listMessages(String systemId) {
        return entityManager.createNamedQuery("Message.listMessages")
                .setParameter("targetSystem", systemId)
                .getResultList();
    }

    public int delete(String systemId, Set<Long> ids) {
        if ( ids.isEmpty() ) {
            // you get an Illegal SQL statement if the set of ids to delete is empty
            return 0;
        }
        return entityManager.createNamedQuery("Message.deleteMessages")
                .setParameter("targetSystem", systemId)
                .setParameter("ids", ids)
                .setParameter("status", MessageStatus.RETRIEVED)
                .executeUpdate();
    }

    @Override
    public List<StatusReport> getStatusReports() {
        //noinspection unchecked
        return entityManager.createQuery(
                "select "
                        + "new se.skltp.messagebox.core.StatusReport(m.targetSystem, m.targetOrganization, m.serviceContract, count(m.serviceContract), min(m.arrived)) "
                        + "from Message m group by m.targetSystem, m.targetOrganization, m.serviceContract"
                        + " order by m.targetSystem, m.targetOrganization, m.serviceContract",
                StatusReport.class)
                .getResultList();
    }

    @Override
    public Message create(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageBody) {
        Message result = new Message(sourceSystem,
                targetSystem,
                targetOrganization,
                serviceContract,
                messageBody,
                MessageStatus.RECEIVED,
                timeService.date()
        );
        store(result);
        return result;
    }


}
