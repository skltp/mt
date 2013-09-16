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

import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.skltp.messagebox.ListMessages.v1.rivtabp21.ListMessagesResponderInterface;
import se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesResponseType;
import se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesType;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.service.MessageService;
import se.skltp.riv.itintegration.messagebox.v1.MessageMetaType;
import se.skltp.riv.itintegration.messagebox.v1.ResultCodeEnum;

@WebService(serviceName = "ListMessagesResponderService", 
            endpointInterface = "se.skltp.messagebox.ListMessages.v1.rivtabp21.ListMessagesResponderInterface",
            portName = "ListMessagesResponderPort", 
            targetNamespace = "urn:riv:itintegration:messagebox:ListMessages:1:rivtabp21",
            wsdlLocation = "schemas/interactions/ListMessagesInteraction/ListMessagesInteraction_1.0_rivtabp21.wsdl")
public class ListMessagesImpl implements ListMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(ListMessagesImpl.class);

    private MessageService messageService;

    private WebServiceContext wsContext;

    @Resource
    public void setWsContext(WebServiceContext wsContext) {
        this.wsContext = wsContext;
    }

    @Resource
    public void setMessageService(MessageService MessageService) {
        this.messageService = MessageService;
    }

    public ListMessagesResponseType listMessages(String logicalAddress, ListMessagesType parameters) {

        ListMessagesResponseType response = new ListMessagesResponseType();
        try {
            //MessageContext msgCtxt = wsContext.getMessageContext();
            //HttpServletRequest req = (HttpServletRequest)msgCtxt.get(MessageContext.SERVLET_REQUEST);

            String systemId = parameters.getSystemId();
            List<Message> messages = messageService.getAllMessagesForSystem(systemId);
                    
            response.setResultCode(ResultCodeEnum.OK);

            for (Message msg : messages) {

                MessageMetaType meta = new MessageMetaType();
                meta.setMessageId(msg.getId());
                meta.setServiceContractType(msg.getServiceContract());
                meta.setTimestamp(msg.getArrived());
                meta.setStatus((msg.getStatus()));

                response.getMessageMetas().add(meta);
            }

        } catch (Exception e) {
            log.warn("Failed to handle ListMessages", e);
            response.setResultCode(ResultCodeEnum.ERROR);      
        }
        return response;
    }
}
