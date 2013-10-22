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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.riv.itintegration.messagebox.ListMessages.v1.ListMessagesResponderInterface;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageMetaType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.riv.itintegration.registry.v1.ServiceContractType;
import se.skltp.messagebox.core.entity.MessageMeta;

@WebService(serviceName = "ListMessagesResponderService",
        endpointInterface = "se.riv.itintegration.messagebox.ListMessages.v1.ListMessagesResponderInterface",
        portName = "ListMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:ListMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/ListMessagesInteraction/ListMessagesInteraction_1.0_rivtabp21.wsdl")
public class ListMessagesImpl extends BaseService implements ListMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(ListMessagesImpl.class);

    @Override
    public ListMessagesResponseType listMessages(String logicalAddress, ListMessagesType parameters) {
        ListMessagesResponseType response = new ListMessagesResponseType();
        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        String targetSystem = extractTargetSystemFromRequest();

        try {

            // the returned list of messages are based on the targetSystem (ie, the callers HSA-ID)
            //
            // The caller can constrain the list by specifying an exact organization id
            // and/or specifying a list of service contract types to be included.
            //
            // if neither is specified, all messages routed to the HSA-ID will be listed.
            //
            // Normally, this would be the desired way of doing things.
            //
            Set<String> serviceContracts = new HashSet<String>();
            for ( ServiceContractType type : parameters.getServiceContractTypes() ) {
                serviceContracts.add(type.getServiceContractNamespace());
            }
            Set<String> targetOrgs = new HashSet<String>(parameters.getTargetOrganizations());

            List<MessageMeta> messages = messageService.listMessages(targetSystem);

            for ( MessageMeta msg : messages ) {

                if ( serviceContractsAllows(serviceContracts, msg) ) {

                    if ( targetOrgsAllows(targetOrgs, msg) ) {

                        MessageMetaType meta = new MessageMetaType();
                        meta.setMessageId(msg.getId());
                        meta.setTargetOrganization(msg.getTargetOrganization());
                        meta.setServiceContractType(msg.getServiceContract());
                        meta.setMessageSize(msg.getMessageBodySize());
                        meta.setArrivalTime(msg.getArrived());
                        meta.setStatus(translateStatusToSchema(msg.getStatus()));

                        response.getMessageMetas().add(meta);
                    }
                }
            }
            

        } catch (Exception e) {
            
            String msg = "Exception for ServiceConsumer " + extractCallingSystemFromRequest() + " when trying to list messages"; 
            logWarn(getLogger(), msg, null, null, e);
            
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorCode.INTERNAL.ordinal());
            response.getResult().setErrorMessage(ErrorCode.INTERNAL.toString());
            response.getMessageMetas().clear();
        }
        return response;
    }

    /**
     * True if the targetOrgs allows the message
     *
     * @param targetOrgs
     * @param msg
     * @return
     */
    private boolean targetOrgsAllows(Set<String> targetOrgs, MessageMeta msg) {
        return targetOrgs.isEmpty() || targetOrgs.contains(msg.getTargetOrganization());
    }

    /**
     *
     * @param serviceContracts
     * @param msg
     * @return
     */
    private boolean serviceContractsAllows(Set<String> serviceContracts, MessageMeta msg) {
        return serviceContracts.isEmpty() || serviceContracts.contains(msg.getServiceContract());
    }
    
    @Override
    public Logger getLogger() {
        return log;
    }

}
