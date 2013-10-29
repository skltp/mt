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
package se.skltp.mb.svc.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.mb.svc.exception.UnreadDeleteException;import se.skltp.mb.types.StatusReport;
import se.skltp.mb.types.entity.MessageMeta;
import se.skltp.mb.types.entity.MessageStatus;
import se.skltp.mb.types.repository.MessageMetaRepository;
import se.skltp.mb.svc.services.MessageService;
import se.skltp.mb.svc.services.StatisticService;

/**
 * Service implementation.
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class) // make sure to rollback for all Exceptions
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMetaRepository messageRepository;

    @Autowired
    private StatisticService statisticService;

    public List<MessageMeta> getMessages(String targetSystem, Collection<Long> ids) {
        List<MessageMeta> messages = messageRepository.getMessages(targetSystem, ids);

        // mark the message as retrieved
        for ( MessageMeta msg : messages ) {
            msg.setStatusRetrieved();
        }

        return messages;

    }

    @Override
    public List<MessageMeta> listMessages(String targetSystem) {
        return messageRepository.listMessages(targetSystem);
    }

    @Override
    public List<MessageMeta> listMessages(String targetSystem, Collection<Long> ids) {
        return messageRepository.listMessages(targetSystem, ids);
    }

    public void saveMessage(MessageMeta message) {
        messageRepository.saveMessage(message);
    }

    @Override
    public MessageMeta create(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageBody, String correlationId) {
        return messageRepository.create(sourceSystem, targetSystem, targetOrganization, serviceContract, messageBody, correlationId);
    }

    public void deleteMessages(String targetSystem, long now, List<MessageMeta> messages)throws UnreadDeleteException {
        // count unread messages and throw an exception if we find any
        List<MessageMeta> unread = new ArrayList<MessageMeta>();
        for ( MessageMeta message : messages ) {
            if ( message.getStatus() != MessageStatus.RETRIEVED ) {
                unread.add(message);
            }
        }
        if (unread.size() > 0) {
            throw new UnreadDeleteException(unread);
        }

        // delete them
        Set<Long> ids = new HashSet<Long>();
        for ( MessageMeta msg : messages ) {
            ids.add(msg.getId());
        }
        // This is a hard delete error because we don't know what ids we failed to delete... note that as we have loaded
        // the messages in the same transaction, they must exist, so this should be a rather severe kind of error
        int numDeleted = messageRepository.deleteMessages(targetSystem, ids);
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
