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
package se.skltp.mt.services;

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.skltp.mt.ListMessages.v1.rivtabp21.ListMessagesResponderInterface;
import se.skltp.mt.ListMessagesresponder.v1.ListMessagesResponseType;
import se.skltp.mt.ListMessagesresponder.v1.ListMessagesType;
import se.skltp.mt.ListMessagesresponder.v1.ResultCodeEnum;
import se.skltp.mt.core.entity.Message;
import se.skltp.mt.core.service.MessageService;
import se.skltp.riv.itintegration.messagebox.v1.MessageMetaType;
import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;

@WebService(serviceName = "ListMessagesResponderService", 
            endpointInterface = "se.skltp.mt.ListMessages.v1.rivtabp20.ListMessagesResponderInterface", 
            portName = "ListMessagesResponderPort", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:ListMessages:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/ListMessagesInteraction/ListMessagesInteraction_1.0_rivtabp20.wsdl")
public class ListMessagesImpl implements ListMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(ListMessagesImpl.class);
    
    private MessageService messageService;

    @Resource
    public void setMessageService(MessageService MessageService) {
        this.messageService = MessageService;
    }

    public ListMessagesResponseType listMessages(String logicalAddress, ListMessagesType parameters) {
        ListMessagesResponseType response = new ListMessagesResponseType();
        try {
            String systemID = parameters.getSystemId();
            List<Message> messages = messageService.getAllMessagesForSystem(systemID);
                    
            response.setResultCode(ResultCodeEnum.OK);

            for (Message msg : messages) {
                MessageMetaType meta = new MessageMetaType();
                meta.setMessageId(msg.getId().toString());
                meta.setServiceContractType(msg.getServiceContract());
                meta.setTimestamp(msg.getArrived());
                meta.setStatus(MessageStatusType.fromValue(msg.getStatus().toString()));

                response.getMessageMetas().add(meta);
            }

        } catch (Exception e) {
            log.warn("Failed to handle ListMessages", e);
            response.setResultCode(ResultCodeEnum.ERROR);      
        }
        return response;
    }
}
