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
package se.skltp.messagebox.types.repository.jpa;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.skltp.messagebox.types.StatusReport;
import se.skltp.messagebox.types.entity.MessageBody;
import se.skltp.messagebox.types.entity.MessageMeta;
import se.skltp.messagebox.types.entity.MessageStatus;
import se.skltp.messagebox.types.repository.MessageMetaRepository;
import se.skltp.messagebox.types.services.TimeService;
import se.vgregion.dao.domain.patterns.repository.db.jpa.DefaultJpaRepository;

@Repository
public class JpaMessageRepository extends DefaultJpaRepository<MessageMeta, Long> implements MessageMetaRepository {

    @Autowired
    TimeService timeService;

    public List<MessageMeta> getMessages(String targetSystem, Collection<Long> ids) {
        if ( ids.isEmpty() ) {
            // you get an Illegal SQL statement if the set of ids to get is empty
            return Collections.emptyList();
        }
        List<MessageBody> bodies = entityManager.createNamedQuery("MessageBody.getMessageBodies", MessageBody.class)
                .setParameter("targetSystem", targetSystem)
                .setParameter("ids", ids)
                .getResultList();
        List<MessageMeta> result = new ArrayList<MessageMeta>(bodies.size());
        for ( MessageBody body : bodies ) {
            body.getMeta().setMessageBody(body);
            result.add(body.getMeta());
        }
        return result;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<MessageMeta> listMessages(String targetSystem) {
        return entityManager.createNamedQuery("Message.listAllMessages")
                .setParameter("targetSystem", targetSystem)
                .getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MessageMeta> listMessages(String targetSystem, Collection<Long> ids) {
        return entityManager.createNamedQuery("Message.listSomeMessages")
                .setParameter("targetSystem", targetSystem)
                .setParameter("ids", ids)
                .getResultList();
    }

    public int deleteMessages(String targetSystem, Set<Long> ids) {
        if ( ids.isEmpty() ) {
            // you get an Illegal SQL statement if the set of ids to delete is empty
            return 0;
        }
        deleteMessageBodies(targetSystem, ids);
        return entityManager.createNamedQuery("Message.deleteMessages")
                .setParameter("targetSystem", targetSystem)
                .setParameter("ids", ids)
                .setParameter("status", MessageStatus.RETRIEVED)
                .executeUpdate();
    }


    @Override
    public int deleteMessageBodies(String targetSystem, Set<Long> ids) {
        if ( ids.isEmpty() ) {
            // you get an Illegal SQL statement if the set of ids to delete is empty
            return 0;
        }
        return entityManager.createNamedQuery("MessageBody.deleteMessageBodies")
                .setParameter("targetSystem", targetSystem)
                .setParameter("ids", ids)
                .setParameter("status", MessageStatus.RETRIEVED)
                .executeUpdate();

    }


    @Override
    public List<StatusReport> getStatusReports() {
        //noinspection unchecked
        return entityManager.createQuery(
                "select "
                        + "new se.skltp.messagebox.types.StatusReport(" +
                        "m.targetSystem," +
                        " m.targetOrganization," +
                        " m.serviceContract," +
                        " count(m.serviceContract)," +
                        " sum(m.messageBodySize)," +
                        " min(m.arrived)) "
                        + "from MessageMeta m group by m.targetSystem, m.targetOrganization, m.serviceContract"
                        + " order by m.targetSystem, m.targetOrganization, m.serviceContract",
                StatusReport.class)
                .getResultList();
    }

    @Override
    public MessageMeta create(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageText, String correlationId) {
        return create(sourceSystem,targetSystem,targetOrganization,serviceContract,messageText,correlationId,MessageStatus.RECEIVED, timeService.date());
    }

    @Override
    public MessageMeta create(String sourceSystem,
                              String targetSystem,
                              String targetOrganization,
                              String serviceContract,
                              String messageText,
                              String correlationId,
                              MessageStatus status,
                              Date arrivalTime) {
        MessageBody body = new MessageBody(messageText);
        MessageMeta result = new MessageMeta(
                sourceSystem,
                targetSystem,
                targetOrganization,
                serviceContract,
                body,
                correlationId, status,
                arrivalTime
        );
        saveMessage(result);
        return result;
    }

    @Override
    public void saveMessage(MessageMeta message) {
        store(message);
        MessageBody body = message.getMessageBody();
        body.setId(message.getId());
        body.setMeta(message);
        entityManager.persist(body);
    }

}
