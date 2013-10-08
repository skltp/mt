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
package se.skltp.messagebox.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.service.TimeService;

@WebService(serviceName = "DeleteMessagesResponderService",
        endpointInterface = "se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface",
        portName = "DeleteMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:DeleteMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/DeleteMessagesInteraction/DeleteMessagesInteraction_1.0_rivtabp21.wsdl")
public class DeleteMessagesImpl extends BaseService implements DeleteMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(DeleteMessagesImpl.class);

    private TimeService timeService;

    @Autowired
    public void setTimeService(TimeService service) {
        this.timeService = service;
    }


    @Override
    public DeleteMessagesResponseType deleteMessages(String logicalAddress, DeleteMessagesType parameters) {
        DeleteMessagesResponseType response = new DeleteMessagesResponseType();
        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        try {
            String targetSystem = extractCallerIdFromRequest();
            Set<Long> messageIdSet = new HashSet<Long>(parameters.getMessageIds());
            List<Message> messages = messageService.getMessages(targetSystem, messageIdSet);
            if ( messageIdSet.size() != messages.size() ) {
                // TODO: See discussion in GetMessagesImpl.java on how to handle this...
                log.info("Receiver " + targetSystem + " attempted to delete non-deletable messages "
                        + describeMessageDiffs(messageIdSet, messages));
            }

            // now delete those messages
            messageService.deleteMessages(targetSystem, timeService.now(), messages);

            List<Long> responseIds = response.getDeletedIds();
            for ( Message msg : messages ) {
                responseIds.add(msg.getId());
            }

        } catch (Exception e) {
            log.warn("Fail!", e);
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorMessage(e.getMessage());
        }
        return response;
    }

}
