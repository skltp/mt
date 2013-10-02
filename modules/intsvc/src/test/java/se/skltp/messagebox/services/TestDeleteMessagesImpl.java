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
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.core.entity.Message;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestDeleteMessagesImpl extends BaseTestImpl {

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

        Set<Long> allEntries = new HashSet<>(Arrays.asList(0L, 1L, 2L));
        Set<Long> middleEntry = new HashSet<>(Arrays.asList(1L));
        Set<Long> remNonExEntry = new HashSet<>(Arrays.asList(1L, 4L, 5L));
        Set<Long> failEntry = new HashSet<>(Arrays.asList(99L));

        // mock up the request
        when(messageService.getMessages("hsaid1", allEntries)).thenReturn(receiver1Messages);
        when(messageService.getMessages("hsaid1", middleEntry)).thenReturn(receiver1Messages.subList(1, 2));
        when(messageService.getMessages("hsaid1", remNonExEntry)).thenReturn(receiver1Messages.subList(1, 2));
        when(messageService.getMessages("hsaid1", failEntry)).thenThrow(new IllegalStateException("fail"));

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);

        DeleteMessagesImpl impl = new DeleteMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);
        impl.setTimeService(timeService);
        DeleteMessagesType params = new DeleteMessagesType();

        // get all for the hsaid1
        when(servletRequest.getHeader(BaseService.HSA_ID_HEADER_NAME)).thenReturn("hsaid1");
        params.getMessageIds().addAll(Arrays.asList(0L, 1L, 2L));
        verifyResponse(receiver1Messages, impl.deleteMessages("mbox-address", params), 0, 1, 2);

        params.getMessageIds().clear();
        params.getMessageIds().addAll(middleEntry);
        verifyResponse(receiver1Messages, impl.deleteMessages("mbox-address", params), 1);

        params.getMessageIds().clear();
        params.getMessageIds().addAll(remNonExEntry);
        verifyResponse(receiver1Messages, impl.deleteMessages("mbox-address", params), 1);

        params.getMessageIds().clear();
        params.getMessageIds().addAll(failEntry);
        DeleteMessagesResponseType responseType = impl.deleteMessages("mbox-address", params);
        assertEquals(ResultCodeEnum.ERROR, responseType.getResult().getCode());
        assertEquals(0, responseType.getDeletedIds().size());


    }

    // trailing ints selects a subset of messages we expect
    private void verifyResponse(List<Message> messages, DeleteMessagesResponseType responseType, Integer... selection) {
        assertEquals(ResultCodeEnum.OK, responseType.getResult().getCode());
        List<Long> deletedIds = responseType.getDeletedIds();
        assertEquals(selection.length == 0 ? messages.size() : selection.length, deletedIds.size());
        Set<Integer> selectionSet = new HashSet<>(Arrays.asList(selection));

        Map<Long, Message> msgMap = new HashMap<>();
        for ( int i = 0, messagesSize = messages.size(); i < messagesSize; i++ ) {
            if ( selection.length == 0 || selectionSet.contains(i) ) {
                msgMap.put(messages.get(i).getId(), messages.get(i));
            }
        }

        for ( Long id : deletedIds ) {
            Message msg = msgMap.get(id);
            assertNotNull("Unexpected message id " + id + " found!", msg);
        }
    }
}
