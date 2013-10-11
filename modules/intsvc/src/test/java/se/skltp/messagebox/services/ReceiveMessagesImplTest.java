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

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import javax.xml.ws.handler.MessageContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceiveMessagesImplTest extends BaseTestImpl {

    // TODO: Test where namespace is defined: in soap env, in soap-body, in content node
    // TODO: Verify that the answer contains the namespace and can be parsed correctly

    /**
     * Simulate receiving one message.
     *
     * @throws Exception
     */
    @Test
    public void testReceive() throws Exception {

        String targetOrg = "targetOrg-HsaId";
        String serviceContractType = "riv:etc,etc...";
        String body = "<body name=\"body\" anotherAttribute=\"a value\">the body text<embeddedNode>with some text</embeddedNode></body>";
        String targetSystem = BaseService.COMMON_TARGET_SYSTEM;

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/ReceiveMessage/" + targetSystem);

        SOAPMessage request = constructCall(targetOrg, serviceContractType, body);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);

        SOAPMessage resultOfCall = impl.invoke(request);

        ArgumentCaptor<String> srcArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> recArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> orgArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> scArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> mbArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> corrIdArg = ArgumentCaptor.forClass(String.class);
        verify(messageService).create(srcArg.capture(), recArg.capture(), orgArg.capture(), scArg.capture(), mbArg.capture(), corrIdArg.capture());

        assertEquals(targetSystem, recArg.getValue());
        assertEquals(serviceContractType, scArg.getValue());
        assertEquals(targetOrg, orgArg.getValue());
        // verify message body
        Node node = XmlUtils.getStringAsDom(mbArg.getValue()).getNode();
        assertEquals(1, node.getChildNodes().getLength());
        Node contentNode = node.getFirstChild();
        assertEquals("content", contentNode.getLocalName());
        assertEquals(serviceContractType, contentNode.getNamespaceURI());
        Node bodyNode = contentNode.getFirstChild();
        assertEquals("body", bodyNode.getLocalName());

        // the answer must be an empty body
        assertEquals(0, resultOfCall.getSOAPBody().getChildNodes().getLength());
    }


    /**
     * Test what happens when we fail to extract the target system id.
     *
     * @throws Exception
     */
    /* INFRA-51: This test is invalid as we no longer extract target system from url
    @Test
    public void testWrongUrl() throws Exception {
        String targetOrg = BaseService.COMMON_TARGET_SYSTEM;
        String legalServiceContractType = "invalid";
        String body = "<body name=\"body\" anotherAttribute=\"a value\">the body text<embeddedNode>with some text</embeddedNode></body>";
        String targetSystem = "receivingOrgHsaId";

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/SomeoneRenamedMyEndpointReceiveMessage/" + targetSystem);

        SOAPMessage request = constructCall(targetOrg, legalServiceContractType, body);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(messageService);
        impl.setWsContext(wsContext);

        try {
            impl.invoke(request);
            fail("Should get exception");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().startsWith("Unable to find my own"));
        }
        verify(messageService, never()).saveMessage((Message) any());
    }
    */


    private SOAPMessage constructCall(String targetOrg, String tkNamespace, String content) throws SOAPException, TransformerException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        message.getSOAPHeader().addHeaderElement(ReceiveMessagesImpl.LOGICAL_ADDRESS_QNAME).addTextNode(targetOrg);

        SOAPBody soapBody = message.getSOAPBody();
        Document doc = soapBody.getOwnerDocument();
        Node node = XmlUtils.getStringAsDom(content).getNode().getChildNodes().item(0);
        Node imported = doc.importNode(node, true);
        SOAPBodyElement contentNode = soapBody.addBodyElement(new QName(tkNamespace, "content"));
        contentNode.appendChild(imported);

        return message;
    }

}
