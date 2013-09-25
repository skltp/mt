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

import se.skltp.messagebox.core.ReceiverState;
import se.skltp.messagebox.core.entity.Message;
import se.vgregion.dao.domain.patterns.repository.Repository;

public interface MessageRepository extends Repository<Message, Long> {

    List<Message> getMessages(String systemId, Set<Long> ids);
    
    List<Message> getMessages(String systemId);

    Long getNumOfMessagesForSystem(String careUnit);

    int delete(String careUnit, Set<Long> ids);

    /**
     * Get statistics for the current state of the receivers.
     *
     * @return an entry for each receiver which has messages waiting to be delivered
     */
    public List<ReceiverState> getReceiverStatus();

}
