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
package se.skltp.messagebox.core.repository;

import java.util.List;
import java.util.Set;

import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.vgregion.dao.domain.patterns.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    List<Message> getMessages(String systemId, Set<Long> ids);

    List<Message> listMessages(String systemId);

    int delete(String careUnit, Set<Long> ids);

    /**
     * Get statistics for the current state of the receivers.
     * <p/>
     * A receiver which has no messages waiting to be delivered is not shown.
     *
     * @return an ordered list of entries for each (receiver, targetOrg, serviceContract) which has messages waiting to be delivered
     */
    public List<StatusReport> getStatusReports();

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


}
