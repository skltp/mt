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

import java.lang.reflect.Field;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesResponseType;
import se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesType;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.service.MessageService;
import se.skltp.riv.itintegration.messagebox.v1.MessageMetaType;
import se.skltp.riv.itintegration.registry.v1.ServiceContractType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestListMessagesImpl {

    @Mock
    MessageService service;

    @Mock
    WebServiceContext wsContext;

    @Mock
    MessageContext msgContext;

    @Mock
    HttpServletRequest servletRequest;

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
                createMessage(idCounter++, "hsaid2", "org4", "tk3", "msg4")
        );


        // mock up the request
        when(service.getAllMessages("hsaid1")).thenReturn(receiver1Messages);
        when(service.getAllMessages("hsaid2")).thenReturn(receiver2Messages);
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        ListMessagesImpl impl = new ListMessagesImpl();
        impl.setMessageService(service);
        impl.setWsContext(wsContext);
        ListMessagesType params = new ListMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.HSA_ID_HEADER_NAME)).thenReturn("hsaid1");
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params));

        // constrain by org
        params.setTargetOrganization("org1");
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0, 1);

        // constrain by org AND tk
        params.getServiceContractTypes().add(createSkt("tk1"));
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0);

        // add another tk
        params.getServiceContractTypes().add(createSkt("tk2"));
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 0, 1);

        // shift org
        params.setTargetOrganization("org2");
        verifyResponse(receiver1Messages, impl.listMessages("mbox-address", params), 2);


        // switch to new HSA-ID for caller
        when(servletRequest.getHeader(BaseService.HSA_ID_HEADER_NAME)).thenReturn("hsaid2");
        // reset parameters and verify we get all the messages
        params.setTargetOrganization(null);
        params.getServiceContractTypes().clear();
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params));

        // various combos of org/tk restrictions ...

        params.setTargetOrganization("org3");
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 0, 1);

        params.getServiceContractTypes().add(createSkt("tk2"));
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 1);

        params.setTargetOrganization("org4");
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 2);

        params.getServiceContractTypes().clear();
        verifyResponse(receiver2Messages, impl.listMessages("mbox-address", params), 2, 3);

    }

    private ServiceContractType createSkt(String serviceContract) {
        ServiceContractType skt = new ServiceContractType();
        skt.setServiceContractNamespace(serviceContract);
        return skt;
    }


    // trailing ints selects a subset of messages we expect
    private void verifyResponse(List<Message> messages, ListMessagesResponseType responseType, Integer... selection) {
        List<MessageMetaType> messageMetas = responseType.getMessageMetas();
        assertEquals(selection.length == 0 ? messages.size() : selection.length, messageMetas.size());
        Set<Integer> selectionSet = new HashSet<>(Arrays.asList(selection));

        Map<Long, Message> msgMap = new HashMap<>();
        for ( int i = 0, messagesSize = messages.size(); i < messagesSize; i++ ) {
            if ( selection.length == 0 || selectionSet.contains(i) ) {
                msgMap.put(messages.get(i).getId(), messages.get(i));
            }
        }

        for ( MessageMetaType meta : messageMetas ) {
            Message msg = msgMap.get(meta.getMessageId());
            assertNotNull("Unexpected message id " + meta.getMessageId() + " found!", msg);
            assertEquals(msg.getServiceContract(), meta.getServiceContractType());
            assertEquals(msg.getStatus(), meta.getStatus());
            assertEquals(msg.getArrived(), meta.getTimestamp());
        }
    }

    // needed to set the message id which is normally generated by the persistance layer
    public static Message createMessage(long id, String receiverId, String targetOrganization, String serviceContract, String body) throws NoSuchFieldException, IllegalAccessException {
        Message result = new Message(receiverId, targetOrganization, serviceContract, body);
        Field field = result.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(result, id);
        return result;
    }
}
