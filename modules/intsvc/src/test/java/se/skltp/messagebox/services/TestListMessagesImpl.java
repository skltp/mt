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

import java.util.*;
import javax.xml.ws.handler.MessageContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesResponseType;
import se.riv.itintegration.messagebox.ListMessagesResponder.v1.ListMessagesType;
import se.riv.itintegration.messagebox.v1.MessageMetaType;
import se.riv.itintegration.messagebox.v1.MessageStatusType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.registry.v1.ServiceContractType;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.MessageStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestListMessagesImpl extends BaseTestImpl {


    /**
     * Verify that the schema status is fully translatable from the entity status.
     *
     * For dependency reasons, we do not want to store the schema-defined enum in the
     * database, but we need to ensure that the entity MessageStatus enum can be fully
     * translated into the schema type.
     */
    @Test
    public void testStatusMapping() {
        assertEquals(MessageStatus.values().length, MessageStatusType.values().length);
        Set<MessageStatusType> schemaStatus = new HashSet<MessageStatusType>();
        for ( MessageStatus messageStatus : MessageStatus.values() ) {
            schemaStatus.add(BaseService.translateStatusToSchema(messageStatus));
        }
        assertEquals(MessageStatusType.values().length, schemaStatus.size());
        schemaStatus.removeAll(Arrays.asList(MessageStatusType.values()));
        assertTrue(schemaStatus.isEmpty());
    }

    /**
     * Verify that we map the parameters and return types correctly when translating
     * between webservice calls and the underlying entity model.
     *
     * @throws Exception
     */
    @Test
    public void testResponse() throws Exception {
        int idCounter = 0;
        List<Message> receiver1Messages = Arrays.asList(
                createMessage(idCounter++, "hsaid1", "org1", "tk1", "msg1"),
                createMessage(idCounter++, "hsaid1", "org1", "tk2", "msg2"),
                createMessage(idCounter++, "hsaid1", "org2", "tk2", "msg3")
        );

        List<Message> receiver2Messages = Arrays.asList(
                createMessage(idCounter++, "hsaid2", "org3", "tk1", "msg1"),
                createMessage(idCounter++, "hsaid2", "org3", "tk2", "msg2"),
                createMessage(idCounter++, "hsaid2", "org4", "tk2", "msg3"),
                createMessage(idCounter, "hsaid2", "org4", "tk3", "msg4")
        );


        // mock up the request
        when(messageService.listMessages("hsaid1")).thenReturn(receiver1Messages);
        when(messageService.listMessages("hsaid2")).thenReturn(receiver2Messages);
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        ListMessagesImpl impl = new ListMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        ListMessagesType params = new ListMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn("hsaid1");
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params));

        // constrain by org
        params.getTargetOrganizations().add("org1");
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0, 1);

        // constrain by org AND tk
        params.getServiceContractTypes().add(createSkt("tk1"));
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0);

        // add another tk
        params.getServiceContractTypes().add(createSkt("tk2"));
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0, 1);

        // add org2
        params.getTargetOrganizations().add("org2");
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0, 1, 2);

        // remove org1
        params.getTargetOrganizations().remove(0);
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 2);

        // switch to new HSA-ID for caller
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn("hsaid2");
        // reset parameters and verify we get all the messages
        params.getTargetOrganizations().clear();
        params.getServiceContractTypes().clear();
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params));

        // various combos of org/tk restrictions ...

        params.getTargetOrganizations().add("org3");
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 0, 1);

        params.getServiceContractTypes().add(createSkt("tk2"));
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 1);

        params.getTargetOrganizations().set(0, "org4");
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 2);

        params.getServiceContractTypes().clear();
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 2, 3);

    }

    @Test
    public void testFailure() throws Exception {

        // mock up the request
        String errorMessage = "Faked exception";
        when(messageService.listMessages("hsaid1")).thenThrow(new RuntimeException(errorMessage));
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        ListMessagesImpl impl = new ListMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        ListMessagesType params = new ListMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn("hsaid1");
        ListMessagesResponseType response = impl.listMessages("mbox-address", params);

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getCode());
        assertEquals(errorMessage, response.getResult().getErrorMessage());
        assertEquals(0, response.getMessageMetas().size());
    }

    private ServiceContractType createSkt(String serviceContract) {
        ServiceContractType skt = new ServiceContractType();
        skt.setServiceContractNamespace(serviceContract);
        return skt;
    }


    // trailing ints selects a subset of messages we expect
    /**
     * Verify
     */
    private void verifyResponse(List<Message> messages, ListMessagesResponseType responseType, Integer... selection) {
        List<MessageMetaType> messageMetas = responseType.getMessageMetas();
        assertEquals(selection.length == 0 ? messages.size() : selection.length, messageMetas.size());
        Set<Integer> selectionSet = new HashSet<Integer>(Arrays.asList(selection));

        Map<Long, Message> msgMap = new HashMap<Long, Message>();
        for ( int i = 0, messagesSize = messages.size(); i < messagesSize; i++ ) {
            if ( selection.length == 0 || selectionSet.contains(i) ) {
                msgMap.put(messages.get(i).getId(), messages.get(i));
            }
        }

        for ( MessageMetaType meta : messageMetas ) {
            Message msg = msgMap.get(meta.getMessageId());
            assertNotNull("Unexpected message id " + meta.getMessageId() + " found!", msg);
            assertEquals(msg.getTargetOrganization(), meta.getTargetOrganization());
            assertEquals(msg.getServiceContract(), meta.getServiceContractType());
            assertEquals(BaseService.translateStatusToSchema(msg.getStatus()), meta.getStatus());
            assertEquals(msg.getArrived(), meta.getArrivalTime());
            assertEquals(msg.getMessageBody().length(), meta.getMessageSize());
        }
    }
}
