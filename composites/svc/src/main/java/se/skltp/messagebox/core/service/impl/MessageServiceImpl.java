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
package se.skltp.messagebox.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.skltp.messagebox.core.service.MessageService;
import se.skltp.messagebox.core.service.StatisticService;

/**
 * Service implementation.
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class) // make sure to rollback for all Exceptions
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatisticService statisticService;

    public List<Message> getMessages(String targetSystem, Set<Long> ids) {
        List<Message> messages = messageRepository.getMessages(targetSystem, ids);

        // mark the message as retrieved
        for ( Message msg : messages ) {
            msg.setStatusRetrieved();
        }

        return messages;

    }

    @Override
    public List<Message> listMessages(String targetSystem) {
        return messageRepository.listMessages(targetSystem);
    }

    public Long saveMessage(Message message) {
        Message result = messageRepository.store(message);
        return result.getId();
    }

    @Override
    public Message create(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageBody) {
        return messageRepository.create(sourceSystem, targetSystem, targetOrganization, serviceContract, messageBody);
    }

    public void deleteMessages(String targetSystem, long now, List<Message> messages) {
        Set<Long> ids = new HashSet<Long>();
        for ( Message msg : messages ) {
            ids.add(msg.getId());
        }
        int numDeleted = messageRepository.delete(targetSystem, ids);
        if ( numDeleted != messages.size() ) {
            throw new IllegalStateException("Unable to delete " + messages.size() + " ids, could only delete " + numDeleted + " ids!");
        }
        statisticService.addDeliveriesToStatistics(targetSystem, now, messages);
    }


    @Override
    public List<StatusReport> getStatusReports() {
        return messageRepository.getStatusReports();
    }

}
