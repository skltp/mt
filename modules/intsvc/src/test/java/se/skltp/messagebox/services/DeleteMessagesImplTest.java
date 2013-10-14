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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.messagebox.core.entity.MessageMeta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteMessagesImplTest extends BaseTestImpl {

    private List<MessageMeta> messages;
    private DeleteMessagesImpl deleteMessages;
    private String targetSys = BaseService.COMMON_TARGET_SYSTEM;
    private String logicalAddress = "mbox-address";
    private DeleteMessagesType params;

    @Before
    public void onSetup() {
        int idCounter = 0;

        try {
            messages = Arrays.asList(
                    createMessage(idCounter++, targetSys, "org1", "tk1", "msg1"),
                    createMessage(idCounter++, targetSys, "org1", "tk2", "msg2"),
                    createMessage(idCounter, targetSys, "org2", "tk2", "msg3")
            );
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn(targetSys);

        deleteMessages = new DeleteMessagesImpl();
        deleteMessages.setMessageService(messageService);
        deleteMessages.setWsContext(wsContext);
        deleteMessages.setTimeService(timeService);

        params = new DeleteMessagesType();
    }

    /**
     * Single message response (middle entry);
     *
     * Make sure the response contains one message, verifying content for correctness.
     *
     * @throws Exception
     */
    @Test
    public void testSingleMessageResponse() throws Exception {

        Collection<Long> middleEntry = Arrays.asList(1L);
        when(messageService.getMessages(targetSys, middleEntry)).thenReturn(messages.subList(1, 2));
        params.getMessageIds().clear();
        params.getMessageIds().addAll(middleEntry);
        messages.get(1).setStatusRetrieved();
        verifyResponse(messages, deleteMessages.deleteMessages(logicalAddress, params), ResultCodeEnum.OK, 1);

    }

    /**
     * Multiple messages (all three)
     *
     * Make sure the response contains three messages, verifying each for correctness.
     *
     * @throws Exception
     */
    @Test
    public void testMultiMessageResponse() throws Exception {

        Collection<Long> allEntries = Arrays.asList(0L, 1L, 2L);
        params.getMessageIds().addAll(allEntries);
        for ( MessageMeta msg : messages ) {
            msg.setStatusRetrieved();
        }
        when(messageService.getMessages(targetSys, allEntries)).thenReturn(messages);
        verifyResponse(messages, deleteMessages.deleteMessages(logicalAddress, params), ResultCodeEnum.OK, 0, 1, 2);
    }


    /**
     * Test how we handle it when the service layer returns a list that are missing some of the ids specified.
     *
     * @throws Exception
     */
    @Test
    public void testIncomplete() throws Exception {

        Collection<Long> remNonExEntry = Arrays.asList(1L, 4L, 5L);
        when(messageService.getMessages(targetSys, remNonExEntry)).thenReturn(messages.subList(1, 2));
        params.getMessageIds().clear();
        params.getMessageIds().addAll(remNonExEntry);
        messages.get(1).setStatusRetrieved(); // mark as delivered
        // an incomplete message returns INFO with an error message
        DeleteMessagesResponseType resp = verifyResponse(messages, deleteMessages.deleteMessages(logicalAddress, params), ResultCodeEnum.INFO, 1);
        assertEquals(DeleteMessagesImpl.INCOMPLETE_ERROR_MESSAGE, resp.getResult().getErrorMessage());
    }

    /**
     * Test what happens when the message service throws an exception.
     *
     * TODO: this is not the final error handling; should generate soap fault
     */
    @Test
    public void testFailure() throws Exception {
        Collection<Long> failEntry = Arrays.asList(99L);
        when(messageService.getMessages(targetSys, failEntry)).thenThrow(new IllegalStateException("fail"));
        params.getMessageIds().clear();
        params.getMessageIds().addAll(failEntry);
        DeleteMessagesResponseType responseType = deleteMessages.deleteMessages(logicalAddress, params);
        assertEquals(ResultCodeEnum.ERROR, responseType.getResult().getCode());
        assertEquals(0, responseType.getDeletedIds().size());
    }

    /**
     * Verify a response vs a selection of a list of messages.
     *
     * We reuse the messages list and selects the answers we expect using the selection list
     */
    private DeleteMessagesResponseType verifyResponse(List<MessageMeta> messages, DeleteMessagesResponseType responseType, ResultCodeEnum code, Integer... selection) {
        assertEquals(code, responseType.getResult().getCode());
        List<Long> deletedIds = responseType.getDeletedIds();
        assertEquals(selection.length == 0 ? messages.size() : selection.length, deletedIds.size());
        Set<Integer> selectionSet = new HashSet<Integer>(Arrays.asList(selection));

        Map<Long, MessageMeta> msgMap = new HashMap<Long, MessageMeta>();
        for ( int i = 0, messagesSize = messages.size(); i < messagesSize; i++ ) {
            if ( selection.length == 0 || selectionSet.contains(i) ) {
                msgMap.put(messages.get(i).getId(), messages.get(i));
            }
        }

        for ( Long id : deletedIds ) {
            MessageMeta msg = msgMap.get(id);
            assertNotNull("Unexpected message id " + id + " found!", msg);
        }
        return responseType;
    }
}
