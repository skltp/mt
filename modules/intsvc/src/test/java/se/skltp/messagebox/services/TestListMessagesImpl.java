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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesResponseType;
import se.skltp.messagebox.ListMessagesresponder.v1.ListMessagesType;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.service.MessageService;
import se.skltp.riv.itintegration.messagebox.v1.MessageMetaType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestListMessagesImpl {

    @Mock
    MessageService service;

    /**
     * Verify that we map the parameters and return types correctly when translating
     * between webservice calls and the underlying entity model.
     *
     * @throws Exception
     */
    @Test
    public void testResponse() throws Exception {
        int idCounter = 0;
        List<Message> system1Messages = Arrays.asList(
                createMessage(idCounter++, "system1", "tk1", "msg1"),
                createMessage(idCounter++, "system1", "tk2", "msg2"),
                createMessage(idCounter++, "system1", "tk3", "msg3")
        );

        List<Message> system2Messages = Arrays.asList(
                createMessage(idCounter++, "system2", "tk1", "msg1"),
                createMessage(idCounter++, "system2", "tk2", "msg2"),
                createMessage(idCounter++, "system2", "tk3", "msg3"),
                createMessage(idCounter++, "system2", "tk4", "msg4")
        );

        when(service.getAllMessagesForSystem("system1")).thenReturn(system1Messages);
        when(service.getAllMessagesForSystem("system2")).thenReturn(system2Messages);

        ListMessagesImpl impl = new ListMessagesImpl();
        impl.setMessageService(service);

        ListMessagesType params = new ListMessagesType();

        params.setSystemId("system1");
        verifyResponse(system1Messages, impl.listMessages("<ntp-address>", params));

        params.setSystemId("system2");
        verifyResponse(system2Messages, impl.listMessages("<ntp-address>", params));

    }


    private void verifyResponse(List<Message> messages, ListMessagesResponseType responseType) {
        List<MessageMetaType> messageMetas = responseType.getMessageMetas();
        assertEquals(messages.size(), messageMetas.size());

        Map<Long, MessageMetaType> mmMap = new HashMap<>();
        for ( MessageMetaType meta : messageMetas ) {
            mmMap.put(meta.getMessageId(), meta);
        }

        for ( Message msg : messages ) {
            MessageMetaType meta = mmMap.get(msg.getId());
            assertNotNull(meta);
            assertEquals(msg.getServiceContract(), meta.getServiceContractType());
            assertEquals(msg.getStatus(), meta.getStatus());
            assertEquals(msg.getArrived(), meta.getTimestamp());
        }
    }

    // needed to set the message id which is normally generated by the persistance layer
    public static Message createMessage(long id, String systemId, String serviceContract, String body) throws NoSuchFieldException, IllegalAccessException {
        Message result = new Message(systemId, serviceContract, body);
        Field field = result.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(result, id);
        return result;
    }
}
