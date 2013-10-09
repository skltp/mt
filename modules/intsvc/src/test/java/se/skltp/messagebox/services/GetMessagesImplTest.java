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
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.core.entity.Message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetMessagesImplTest extends BaseTestImpl {


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
                createMessage(idCounter, "hsaid1", "org2", "tk2", "msg3")
        );

        Set<Long> allEntries = new HashSet<Long>(Arrays.asList(0L, 1L, 2L));
        Set<Long> middleEntry = new HashSet<Long>(Arrays.asList(1L));

        // mock up the request
        when(messageService.getMessages("hsaid1", allEntries)).thenReturn(receiver1Messages);
        when(messageService.getMessages("hsaid1", middleEntry)).thenReturn(receiver1Messages.subList(1, 2));
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        GetMessagesImpl impl = new GetMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        GetMessagesType params = new GetMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn("hsaid1");
        params.getMessageIds().addAll(Arrays.asList(0L, 1L, 2L));
        verifyResponse(receiver1Messages, impl.getMessages("mbox-address", params), 0, 1, 2);

        params.getMessageIds().remove(0);
        params.getMessageIds().remove(1);
        verifyResponse(receiver1Messages, impl.getMessages("mbox-address", params), 1);

        // switch to new HSA-ID for caller
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn("hsaid2");

        // trying to get messages we do not own just ignores it (log it as info)
        GetMessagesResponseType response = impl.getMessages("mbox-address", params);
        assertEquals(ResultCodeEnum.OK, response.getResult().getCode());
        assertTrue(response.getResponses().isEmpty());
    }

    @Test
    public void testFailure() throws Exception {
        Set<Long> allEntries = new HashSet<Long>(Arrays.asList(0L, 1L, 2L));

        // mock up the request
        String errorMessage = "failed";
        when(messageService.getMessages("hsaid1", allEntries)).thenThrow(new RuntimeException(errorMessage));
        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        GetMessagesImpl impl = new GetMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        GetMessagesType params = new GetMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn("hsaid1");
        params.getMessageIds().addAll(Arrays.asList(0L, 1L, 2L));
        GetMessagesResponseType resp = impl.getMessages("mbox-address", params);

        assertEquals(ResultCodeEnum.ERROR, resp.getResult().getCode());
        assertEquals(errorMessage, resp.getResult().getErrorMessage());
        assertEquals(0, resp.getResponses().size());
    }

    // trailing ints selects a subset of messages we expect
    private void verifyResponse(List<Message> messages, GetMessagesResponseType responseType, Integer... selection) {
        assertEquals(ResultCodeEnum.OK, responseType.getResult().getCode());
        List<ResponseType> responses = responseType.getResponses();
        assertEquals(selection.length == 0 ? messages.size() : selection.length, responses.size());
        Set<Integer> selectionSet = new HashSet<Integer>(Arrays.asList(selection));

        Map<Long, Message> msgMap = new HashMap<Long, Message>();
        for ( int i = 0, messagesSize = messages.size(); i < messagesSize; i++ ) {
            if ( selection.length == 0 || selectionSet.contains(i) ) {
                msgMap.put(messages.get(i).getId(), messages.get(i));
            }
        }

        for ( ResponseType response : responses ) {
            Message msg = msgMap.get(response.getMessageId());
            assertNotNull("Unexpected message id " + response.getMessageId() + " found!", msg);
            assertEquals(msg.getTargetOrganization(), response.getTargetOrganization());
            assertEquals(msg.getServiceContract(), response.getServiceContractType().getServiceContractNamespace());
            assertEquals(msg.getMessageBody(), response.getMessage());
        }
    }
}
