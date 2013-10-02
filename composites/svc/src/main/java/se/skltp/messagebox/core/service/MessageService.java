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
package se.skltp.messagebox.core.service;

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
     * @param receiverId hsa-id for receiving system
     * @param ids        to get
     * @return list of messages with the given ids belonging to receiverId
     *         (asking for ids not belonging to the receiverId will be silentrly ignored)
     */
    List<Message> getMessages(String receiverId, Set<Long> ids);

    /**
     * List messages available for the given receiver.
     *
     * @param receiverId hsa-id for reciving system.
     * @return list of messages
     */
    List<Message> listMessages(String receiverId);

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
     * @param sourceSystem hsa-id of source system
     * @param receiverSystem hsa-id of receiving system
     * @param targetOrganization hsa-id of target org
     * @param serviceContract of message
     * @param messageBody message body
     * @param correlationId business correlation id
     * @return created message, with arrival time set to TimeService.now() and status {@link se.riv.itintegration.messagebox.v1.MessageStatusType#RECEIVED}
     */
    Message create(String sourceSystem, String receiverSystem, String targetOrganization, String serviceContract, String messageBody, String correlationId);



    /**
     * Delete all identified messages, returning how many were actually deleted.
     *
     * @param receiverId all messages must have this receiverId
     * @param timestamp
     * @param messages
     */
    void deleteMessages(String receiverId, long timestamp, List<Message> messages);


    /**
     * Get statistics for the current state of the receivers.
     * <p/>
     * A receiver which has no messages waiting to be delivered is not shown.
     *
     * @return an ordered list of entries for each (receiver, targetOrg, serviceContract) which has messages waiting to be delivered
     */
    public List<StatusReport> getStatusReports();
}
