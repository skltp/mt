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
import se.riv.itintegration.messagebox.v1.MessageMetaType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.riv.itintegration.registry.v1.ServiceContractType;
import se.riv.messagebox.ListMessages.v1.rivtabp21.ListMessagesResponderInterface;
import se.riv.messagebox.ListMessagesresponder.v1.ListMessagesResponseType;
import se.riv.messagebox.ListMessagesresponder.v1.ListMessagesType;
import se.skltp.messagebox.core.entity.Message;

@WebService(serviceName = "ListMessagesResponderService",
        endpointInterface = "se.riv.messagebox.ListMessages.v1.rivtabp21.ListMessagesResponderInterface",
        portName = "ListMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:ListMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/ListMessagesInteraction/ListMessagesInteraction_1.0_rivtabp21.wsdl")
public class ListMessagesImpl extends BaseService implements ListMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(ListMessagesImpl.class);

    public ListMessagesResponseType listMessages(String logicalAddress, ListMessagesType parameters) {

        ListMessagesResponseType response = new ListMessagesResponseType();
        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        try {
            String hsaId = extractCallerIdFromRequest();

            // the returned list of messages are based on the callers HSA-ID
            //
            // The caller can constrain the list by specifying an exact organization id
            // and/or specifying a list of service contract types to be included.
            //
            // if neither is specified, all messages routed to the HSA-ID will be listed.
            //
            // Normally, this would be the desired way of doing things.
            //
            Set<String> types = new HashSet<>();
            for ( ServiceContractType type : parameters.getServiceContractTypes() ) {
                types.add(type.getServiceContractNamespace());
            }
            Set<String> targetOrgs = new HashSet<>(parameters.getTargetOrganizations());

            List<Message> messages = messageService.listMessages(hsaId);

            for ( Message msg : messages ) {

                if ( types.isEmpty() || types.contains(msg.getServiceContract()) ) {

                    if ( targetOrgs.isEmpty() || targetOrgs.contains(msg.getTargetOrganization()) ) {

                        MessageMetaType meta = new MessageMetaType();
                        meta.setMessageId(msg.getId());
                        meta.setTargetOrganization(msg.getTargetOrganization());
                        meta.setServiceContractType(msg.getServiceContract());
                        meta.setMessageSize(msg.getMessageBody().length());
                        meta.setArrivalTime(msg.getArrived());
                        meta.setStatus((msg.getStatus()));

                        response.getMessageMetas().add(meta);
                    }
                }
            }

        } catch (Exception e) {
            log.warn("Fail!", e);
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorMessage(e.getMessage());
            response.getMessageMetas().clear();
        }
        return response;
    }

}
