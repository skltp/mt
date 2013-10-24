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
package se.skltp.messagebox.types.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import se.skltp.messagebox.types.StatusReport;
import se.skltp.messagebox.types.entity.MessageMeta;
import se.skltp.messagebox.types.entity.MessageStatus;
import se.vgregion.dao.domain.patterns.repository.Repository;

/**
 * Repository for MessageMetas.
 * <p/>
 * A Message consists of meta-data about the message {@link MessageMeta} and the body of the message itself
 * {@link se.skltp.messagebox.types.entity.MessageBody}.
 * <p/>
 * The split is due to optimization - we do not want to load the body of the message when listing or deleting
 * messages.
 * <p/>
 * Creating, loading and deleting message bodies is under the control of the MessageMeta class.
 */
public interface MessageMetaRepository extends Repository<MessageMeta, Long> {

    List<MessageMeta> getMessages(String targetSystem, Collection<Long> ids);

    List<MessageMeta> listMessages(String targetSystem);

    List<MessageMeta> listMessages(String targetSystem, Collection<Long> ids);

    int deleteMessages(String targetSystem, Set<Long> ids);

    /**
     * Get statistics for the current state of the target systems.
     * <p/>
     * A receiver which has no messages waiting to be delivered is not shown.
     *
     * @return an ordered list of entries for each (receiver, targetOrg, serviceContract) which has messages waiting to be delivered
     */
    public List<StatusReport> getStatusReports();

    /**
     * As the full create, with status RECEIVED and arrival time now.
     *
     * @return created message, with arrival time set to TimeService.now() and status {@link se.skltp.messagebox.types.entity.MessageStatus#RECEIVED}
     */
    MessageMeta create(String sourceSystem,
                       String targetSystem,
                       String targetOrganization,
                       String serviceContract,
                       String messageBody,
                       String correlationId);

    /**
     * Create a messsage and return its message-meta.
     *
     * @param sourceSystem       id for originating system
     * @param targetSystem       id for target system
     * @param targetOrganization id of target for message
     * @param serviceContract    type of message
     * @param messageBody        text of message
     * @param correlationId      id of message
     * @param status             of message
     * @param arrivalTime        when it arrived
     * @return created message-meta
     */
    MessageMeta create(String sourceSystem,
                       String targetSystem,
                       String targetOrganization,
                       String serviceContract,
                       String messageBody,
                       String correlationId,
                       MessageStatus status,
                       Date arrivalTime);


    void saveMessage(MessageMeta message);

    /**
     * Delete the message bodies (only!) for the given messages
     *
     * @param targetSystem
     * @param ids
     */
    int deleteMessageBodies(String targetSystem, Set<Long> ids);

}
