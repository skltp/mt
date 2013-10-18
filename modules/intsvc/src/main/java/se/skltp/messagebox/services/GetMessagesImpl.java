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

import se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.riv.itintegration.registry.v1.ServiceContractType;
import se.skltp.messagebox.core.entity.MessageMeta;

@WebService(serviceName = "GetMessagesResponderService",
        endpointInterface = "se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface",
        portName = "GetMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:GetMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/GetMessagesInteraction/GetMessagesInteraction_1.0_rivtabp21.wsdl")
public class GetMessagesImpl extends BaseService implements GetMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(GetMessagesImpl.class);
    public static final String INCOMPLETE_ERROR_MESSAGE = "Incomplete Get";

    @Override
    public GetMessagesResponseType getMessages(String logicalAddress, GetMessagesType parameters) {
        GetMessagesResponseType response = new GetMessagesResponseType();
        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        String targetSystem = extractTargetSystemFromRequest();

        try {

            List<MessageMeta> messages = messageService.getMessages(targetSystem, parameters.getMessageIds());

            if ( parameters.getMessageIds().size() != messages.size() ) {
                String msg = "Target system " + targetSystem + " attempted to get non-present messages " + describeMessageDiffs(parameters.getMessageIds(), messages);
                logWarn(msg, null, this, null);
                
                response.getResult().setCode(ResultCodeEnum.INFO);
                response.getResult().setErrorMessage(INCOMPLETE_ERROR_MESSAGE);

            }

            for ( MessageMeta msg : messages ) {

                ResponseType elem = new ResponseType();
                elem.setMessageId(msg.getId());
                ServiceContractType serverContract = new ServiceContractType();
                serverContract.setServiceContractNamespace(msg.getServiceContract());
                elem.setServiceContractType(serverContract);
                elem.setTargetOrganization(msg.getTargetOrganization());

                elem.setMessage(msg.getMessageBody().getText());

                response.getResponses().add(elem);
            }
            
            // Log read messages
            for(ResponseType elem :  response.getResponses()) {
                String msgId = String.valueOf(elem.getMessageId());
                logInfo("Message " + msgId + " was read by " + targetSystem, msgId, this);
            }

        } catch (Exception e) {
            
            String msg = "Exception for ServiceConsumer " + extractCallingSystemFromRequest() + " when trying to get messages"; 
            logWarn(msg, null, this, e);
            
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorCode.INTERNAL.ordinal());
            response.getResult().setErrorMessage(ErrorCode.INTERNAL.toString());
            response.getResponses().clear();
        }
        return response;
    }
    
    @Override
    public Logger getLogger() {
        return log;
    }

}
