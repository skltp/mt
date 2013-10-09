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
import org.w3c.dom.Document;
import se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.riv.itintegration.registry.v1.ServiceContractType;
import se.skltp.messagebox.core.entity.Message;

@WebService(serviceName = "GetMessagesResponderService",
        endpointInterface = "se.riv.itintegration.messagebox.GetMessages.v1.GetMessagesResponderInterface",
        portName = "GetMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:GetMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/GetMessagesInteraction/GetMessagesInteraction_1.0_rivtabp21.wsdl")
public class GetMessagesImpl extends BaseService implements GetMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(GetMessagesImpl.class);

    @Override
    public GetMessagesResponseType getMessages(
            String logicalAddress,
            GetMessagesType parameters) {
        GetMessagesResponseType response = new GetMessagesResponseType();

        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        try {
            String targetSystem = extractCallerIdFromRequest();
            Set<Long> messageIdSet = new HashSet<Long>(parameters.getMessageIds());

            List<Message> messages = messageService.getMessages(targetSystem, messageIdSet);

            if ( messageIdSet.size() != messages.size() ) {
                // TODO: this is the "optimistic" way of doing it, getting those you could get
                // and letting the user decide if he wants to treat the diff as a mismatch.
                // However, it might be better to treat it as an error in order for things to
                // fail fast and not deceived the user into thinking an "OK" meant that all was
                // well.... (also, during testing, if you forget to set the targetSystem hsa-id
                // in the header, you get back an "OK" and no messages...
                // This also applies to DeleteMessagesImpl
                log.info("Receiver " + targetSystem + " attempted to get non-present messages "
                        + describeMessageDiffs(messageIdSet, messages));
            }

            for ( Message msg : messages ) {

                ResponseType elem = new ResponseType();
                elem.setMessageId(msg.getId());
                ServiceContractType serverContract = new ServiceContractType();
                serverContract.setServiceContractNamespace(msg.getServiceContract());
                elem.setServiceContractType(serverContract);
                elem.setTargetOrganization(msg.getTargetOrganization());

                // TODO: Decide if we should transfer the message body as a string
                elem.setMessage(msg.getMessageBody());
                // TODO: ... or as the "ANY" field
                Document doc = (Document) XmlUtils.getStringAsDom(msg.getMessageBody()).getNode();
                elem.setAny(doc.getDocumentElement());
                // TODO: either decide or add a parameter to select

                response.getResponses().add(elem);
            }


        } catch (Exception e) {
            log.warn("Fail!", e);
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorMessage(e.getMessage());
            response.getResponses().clear();
        }
        return response;
    }

}
