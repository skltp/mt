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
package se.skltp.messagebox.services;

import java.util.List;
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
            String targetSystem = extractCallingSystemFromRequest();
            List<Message> messages = messageService.getMessages(targetSystem, parameters.getMessageIds());
            if ( parameters.getMessageIds().size() != messages.size() ) {
                // TODO: See discussion in GetMessagesImpl.java on how to handle this...
                log.info("Receiver " + targetSystem + " attempted to delete non-deletable messages "
                        + describeMessageDiffs(parameters.getMessageIds(), messages));
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
