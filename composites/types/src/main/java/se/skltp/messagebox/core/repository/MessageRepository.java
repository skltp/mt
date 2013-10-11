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
package se.skltp.messagebox.core.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.vgregion.dao.domain.patterns.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    List<Message> getMessages(String systemId, Collection<Long> ids);

    List<Message> listMessages(String systemId);

    int delete(String careUnit, Set<Long> ids);

    /**
     * Get statistics for the current state of the target systems.
     * <p/>
     * A receiver which has no messages waiting to be delivered is not shown.
     *
     * @return an ordered list of entries for each (receiver, targetOrg, serviceContract) which has messages waiting to be delivered
     */
    public List<StatusReport> getStatusReports();

    /**
     * Create a message with
     *
     *
     * @param sourceSystem hsa-id of source system
     * @param targetSystem hsa-id of target system
     * @param targetOrganization hsa-id of target org
     * @param serviceContract of message
     * @param messageBody message body
     * @return created message, with arrival time set to TimeService.now() and status {@link se.skltp.messagebox.core.entity.MessageStatus#RECEIVED}
     */
    Message create(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageBody);


}
