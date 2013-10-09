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
package se.skltp.messagebox.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;

public interface MessageService {

    /**
     * Get messages for delivery to the user.
     * <p/>
     * Will mark the messages as delivered.
     *
     *
     * @param targetSystem hsa-id for receiving system
     * @param ids        to get
     * @return list of messages with the given ids belonging to targetSystem
     *         (asking for ids not belonging to the targetSystem will be silentrly ignored)
     */
    List<Message> getMessages(String targetSystem, Collection<Long> ids);

    /**
     * List messages available for the given target system.
     *
     * @param targetSystem hsa-id for target system.
     * @return list of messages
     */
    List<Message> listMessages(String targetSystem);

    /**
     * SaveOrPersist a message.
     *
     * @param message to persist
     * @return id of message
     */
    Long saveMessage(Message message);

    /**
     * Create a message with
     *
     *
     * @param sourceSystem hsa-id of source system
     * @param targetSystem hsa-id of receiving system
     * @param targetOrganization hsa-id of target org
     * @param serviceContract of message
     * @param messageBody message body
     * @return created message, with arrival time set to TimeService.now() and status {@link se.riv.itintegration.messagebox.v1.MessageStatusType#RECEIVED}
     */
    Message create(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageBody);



    /**
     * Delete all identified messages, returning how many were actually deleted.
     *
     * @param targetSystem all messages must have this targetSystem
     * @param now current time (to determine delivery time for statistic purposes)
     * @param messages to delete
     */
    void deleteMessages(String targetSystem, long now, List<Message> messages);


    /**
     * Get statistics for the current state of the target systems.
     * <p/>
     * A target system which has no messages waiting to be delivered is not shown.
     *
     * @return an ordered list of entries for each (targetSyst, targetOrg, serviceContract) which has messages waiting to be delivered
     */
    public List<StatusReport> getStatusReports();
}
