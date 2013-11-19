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
package se.skltp.mb.intsvc;

import java.util.*;
import javax.xml.ws.handler.MessageContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.GetMessagesType;
import se.riv.infrastructure.itintegration.messagebox.GetMessagesResponder.v1.ResponseType;
import se.riv.infrastructure.itintegration.messagebox.v1.ResultCodeEnum;
import se.skltp.mb.types.entity.MessageMeta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetMessagesImplTest extends BaseTestImpl {

    private String targetSys = BaseService.COMMON_TARGET_SYSTEM;
    private String logicalAddress = "mbox-address";
    private List<MessageMeta> messages;
    private GetMessagesImpl getMessagesImpl;
    private GetMessagesType params;


    @Before
    public void onSetup() throws NoSuchFieldException, IllegalAccessException {
        int idCounter = 0;

        messages = Arrays.asList(
                createMessage(idCounter++, targetSys, "org1", "tk1", "msg1"),
                createMessage(idCounter++, targetSys, "org1", "tk2", "msg2"),
                createMessage(idCounter, targetSys, "org2", "tk2", "msg3")
        );

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getHeader(BaseService.SERVICE_CONSUMER_HSA_ID_HEADER_NAME)).thenReturn(targetSys);

        getMessagesImpl = new GetMessagesImpl();
        getMessagesImpl.setMessageService(messageService);
        getMessagesImpl.setWsContext(wsContext);

        params = new GetMessagesType();
    }

    /**
     * Verify that we respond with one message, with the expected data.
     *
     * @throws Exception
     */
    @Test
    public void testOneMessage() throws Exception {
        Collection<Long> middleEntry = Arrays.asList(1L);

        when(messageService.getMessages(targetSys, middleEntry)).thenReturn(messages.subList(1, 2));

        params.getMessageIds().addAll(middleEntry);
        verifyResponse(messages, getMessagesImpl.getMessages(logicalAddress, params), ResultCodeEnum.OK, 1);
    }

    /**
     * Verify that we respond with three correct entries for the three messages.
     *
     * @throws Exception
     */
    @Test
    public void testThreeMessages() throws Exception {
        Collection<Long> allEntries = Arrays.asList(0L, 1L, 2L);

        when(messageService.getMessages(targetSys, allEntries)).thenReturn(messages);

        params.getMessageIds().addAll(allEntries);
        verifyResponse(messages, getMessagesImpl.getMessages(logicalAddress, params), ResultCodeEnum.OK, 0, 1, 2);
    }


    /**
     * Verify that an incomplete get responds with a partial response and an INFO code.
     *
     * @throws Exception
     */
    @Test
    public void testIncomplete() throws Exception {
        Collection<Long> someNonExEntries = Arrays.asList(1L, 4L, 5L);
        when(messageService.getMessages(targetSys, someNonExEntries)).thenReturn(messages.subList(1, 2));
        params.getMessageIds().clear();
        params.getMessageIds().addAll(someNonExEntries);
        // an incomplete message returns INFO with an error message
        GetMessagesResponseType resp = verifyResponse(messages, getMessagesImpl.getMessages(logicalAddress, params), ResultCodeEnum.INFO, 1);
        assertEquals(GetMessagesImpl.INCOMPLETE_ERROR_MESSAGE, resp.getResult().getErrorMessage());

    }

    /**
     * Test how we package an error-result.
     *
     * @throws Exception
     */
    @Test
    public void testFailure() throws Exception {
        Collection<Long> idsTriggeringFailure = Arrays.asList(0L);

        // mock up the request to throw an exception when we ask for messages with the idsTriggeringFailure argument
        when(messageService.getMessages(targetSys, idsTriggeringFailure)).thenThrow(new RuntimeException("something"));

        params.getMessageIds().addAll(idsTriggeringFailure);
        GetMessagesResponseType resp = getMessagesImpl.getMessages(logicalAddress, params);

        assertEquals(ResultCodeEnum.ERROR, resp.getResult().getCode());
        assertEquals(ErrorCodes.INTERNAL.toString(), resp.getResult().getErrorMessage());
        assertEquals(0, resp.getResponses().size());
    }

    /**
     * Verify a response vs a selection of a list of messages.
     * <p/>
     * We reuse the messages list and selects the answers we expect using the selection list
     */
    private GetMessagesResponseType verifyResponse(List<MessageMeta> messages, GetMessagesResponseType responseType, ResultCodeEnum code, Integer... selection) {
        assertEquals(code, responseType.getResult().getCode());
        List<ResponseType> responses = responseType.getResponses();
        assertEquals(selection.length == 0 ? messages.size() : selection.length, responses.size());
        Set<Integer> selectionSet = new HashSet<Integer>(Arrays.asList(selection));

        Map<Long, MessageMeta> msgMap = new HashMap<Long, MessageMeta>();
        for ( int i = 0, messagesSize = messages.size(); i < messagesSize; i++ ) {
            if ( selection.length == 0 || selectionSet.contains(i) ) {
                msgMap.put(messages.get(i).getId(), messages.get(i));
            }
        }

        for ( ResponseType response : responses ) {
            MessageMeta msg = msgMap.get(response.getMessageId());
            assertNotNull("Unexpected message id " + response.getMessageId() + " found!", msg);
            assertEquals(msg.getTargetOrganization(), response.getTargetOrganization());
            assertEquals(msg.getServiceContract(), response.getServiceContractType().getServiceContractNamespace());
            assertEquals(msg.getMessageBody().getText(), response.getMessage());
        }

        return responseType;
    }
}
