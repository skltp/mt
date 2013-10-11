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
public class ListMessagesImplTest extends BaseTestImpl {


    /**
     * Verify that the schema status is fully translatable from the entity status.
     * <p/>
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
     * Check how the constraints on the list messages work.
     * <p/>
     * Ie. if you specify targetOrgs or serviceContracts, you only get the messages that
     * match all the constraints.
     *
     * @throws Exception
     */
    @Test
    public void testConstraints() throws Exception {
        int idCounter = 0;
        String addr = "mbox-address";
        String targetSys1 = BaseService.COMMON_TARGET_SYSTEM;
        List<Message> sys1Messages = Arrays.asList(
                createMessage(idCounter++, targetSys1, "org1", "tk1", "msg1"),
                createMessage(idCounter++, targetSys1, "org1", "tk2", "msg2"),
                createMessage(idCounter++, targetSys1, "org2", "tk2", "msg3")
        );

        /**
         * INFRA-51: does not support multiple mailboxes, so we need to adjust the tests to use a common targetSystem
        String targetSys2 = "targetSys2-hsaId";
        List<Message> receiver2Messages = Arrays.asList(
                createMessage(idCounter++, targetSys2, "org3", "tk1", "msg1"),
                createMessage(idCounter++, targetSys2, "org3", "tk2", "msg2"),
                createMessage(idCounter++, targetSys2, "org4", "tk2", "msg3"),
                createMessage(idCounter, targetSys2, "org4", "tk3", "msg4")
        );
         */


        // mock up the request
        when(messageService.listMessages(targetSys1)).thenReturn(sys1Messages);
        // INFRA-51: when(messageService.listMessages(targetSys2)).thenReturn(receiver2Messages);
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        ListMessagesImpl impl = new ListMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        ListMessagesType params = new ListMessagesType();

        // get all for the targetSys1
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn(targetSys1);

        verifyResponse(sys1Messages, impl.listMessages(addr, params));

        // constrain by org
        params.getTargetOrganizations().add("org1");
        verifyResponse(sys1Messages, impl.listMessages(addr, params), 0, 1);

        // constrain by org AND tk
        params.getServiceContractTypes().add(createSkt("tk1"));
        verifyResponse(sys1Messages, impl.listMessages(addr, params), 0);

        // add another tk
        params.getServiceContractTypes().add(createSkt("tk2"));
        verifyResponse(sys1Messages, impl.listMessages(addr, params), 0, 1);

        // add org2
        params.getTargetOrganizations().add("org2");
        verifyResponse(sys1Messages, impl.listMessages(addr, params), 0, 1, 2);

        // remove org1
        params.getTargetOrganizations().remove(0);
        verifyResponse(sys1Messages, impl.listMessages(addr, params), 2);

        /** INFRA-51: Can't use a second targetSystem
        // switch to new HSA-ID for caller
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn(targetSys2);

        // reset parameters and verify we get all the messages
        params.getTargetOrganizations().clear();
        params.getServiceContractTypes().clear();
        verifyResponse(receiver2Messages, impl.listMessages(addr, params));

        // various combos of org/tk restrictions ...

        params.getTargetOrganizations().add("org3");
        verifyResponse(receiver2Messages, impl.listMessages(addr, params), 0, 1);

        params.getServiceContractTypes().add(createSkt("tk2"));
        verifyResponse(receiver2Messages, impl.listMessages(addr, params), 1);

        params.getTargetOrganizations().set(0, "org4");
        verifyResponse(receiver2Messages, impl.listMessages(addr, params), 2);

        params.getServiceContractTypes().clear();
        verifyResponse(receiver2Messages, impl.listMessages(addr, params), 2, 3);
         */

    }

    @Test
    public void testFailure() throws Exception {

        // mock up the request
        String errorMessage = "Faked exception";
        String targetSys = BaseService.COMMON_TARGET_SYSTEM;
        when(messageService.listMessages(targetSys)).thenThrow(new RuntimeException(errorMessage));
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        ListMessagesImpl impl = new ListMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        ListMessagesType params = new ListMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn(targetSys);
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


    /**
     * Verify a response vs a selection of a list of messages.
     * <p/>
     * We reuse the messages list and selects the answers we expect using the selection list
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
