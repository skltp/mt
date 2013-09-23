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

import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.exception.InvalidServiceContractTypeException;

public interface MessageService {

    List<Message> getMessages(String receiverId, Set<Long> ids);

    List<Message> getAllMessages(String receiverId);

    Long saveMessage(Message Message);

    /**
     * Delete all identified messages, returning how many were actually deleted.
     *
     * @param receiverId all messages must have this receiverId
     * @param ids set of messages to delete
     */
    void deleteMessages(String receiverId, Set<Long> ids);

    /**
     * Return the xml-body for the response when this message is successfully stored.
     *
     * @param serviceContractType the service contract name space
     * @return response-body to be used in the response
     * @trhrows InvalidServiceContractTypeException if the service contract cannot be used in the message box
     */
    public String getOkResponseForServiceContract(String serviceContractType) throws InvalidServiceContractTypeException;

}
