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
import java.util.Set;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.skltp.messagebox.DeleteMessages.v1.rivtabp21.DeleteMessagesResponderInterface;
import se.skltp.messagebox.DeleteMessagesresponder.v1.DeleteMessagesResponseType;
import se.skltp.messagebox.DeleteMessagesresponder.v1.DeleteMessagesType;
import se.skltp.riv.itintegration.messagebox.v1.ResultCodeEnum;

@WebService(serviceName = "DeleteMessagesResponderService",
        endpointInterface = "se.skltp.messagebox.DeleteMessages.v1.rivtabp21.DeleteMessagesResponderInterface",
        portName = "DeleteMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:DeleteMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/DeleteMessagesInteraction/DeleteMessagesInteraction_1.0_rivtabp21.wsdl")
public class DeleteMessagesImpl extends BaseService implements DeleteMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(DeleteMessagesImpl.class);


    @Override
    public DeleteMessagesResponseType deleteMessages(String logicalAddress, DeleteMessagesType parameters) {
        DeleteMessagesResponseType response = new DeleteMessagesResponseType();
        response.setResultCode(ResultCodeEnum.OK);

        String receiverId = extractCallerIdFromRequest();
        Set<Long> messageIdSet = new HashSet<>(parameters.getMessageIds());

        try {

            messageService.deleteMessages(receiverId, messageIdSet);

        } catch (Exception e) {
            log.warn("Fail!", e);
            response.setResultCode(ResultCodeEnum.ERROR);
        }
        return response;
    }

}
