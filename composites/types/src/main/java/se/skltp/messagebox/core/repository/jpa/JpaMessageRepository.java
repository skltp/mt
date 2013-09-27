/*
 * Copyright 2010 Inera
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *
 *   Boston, MA 02111-1307  USA
 */
package se.skltp.messagebox.core.repository.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

@Repository
public class JpaMessageRepository extends DefaultJpaRepository<Message, Long> implements MessageRepository {

    @SuppressWarnings("unchecked")
    public List<Message> getMessages(String systemId, Set<Long> ids) {
        return entityManager.createNamedQuery("Message.getForReceiverWithIds")
                .setParameter("systemId", systemId)
                .setParameter("ids", ids)
                .getResultList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Message> listMessages(String systemId) {
        return entityManager.createNamedQuery("Message.getForReceiver")
                .setParameter("systemId", systemId)
                .getResultList();
    }

    public int delete(String systemId, Set<Long> ids) {
        if ( ids.isEmpty() ) {
            // you get an Illegal SQL statement if the set of ids to delete is empty
            return 0;
        }
        return entityManager.createNamedQuery("Message.deleteForReceiverWithIdsAndStatus")
                .setParameter("systemId", systemId)
                .setParameter("ids", ids)
                .setParameter("status", MessageStatusType.RETRIEVED)
                .executeUpdate();
    }

    @Override
    public List<StatusReport> getStatusReports() {
        //noinspection unchecked
        return entityManager.createQuery(
                "select "
                        + "new se.skltp.messagebox.core.StatusReport(m.receiverId, m.targetOrganization, m.serviceContract, count(m.serviceContract), min(m.arrived)) "
                        + "from Message m group by m.receiverId, m.targetOrganization, m.serviceContract"
                        + " order by m.receiverId, m.targetOrganization, m.serviceContract",
                StatusReport.class)
                .getResultList();
    }

}
