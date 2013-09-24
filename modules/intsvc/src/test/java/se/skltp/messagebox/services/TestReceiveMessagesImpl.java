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

import java.io.ByteArrayInputStream;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.exception.InvalidServiceContractTypeException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestReceiveMessagesImpl extends BaseTestImpl {


    /**
     * Verify that we map the parameters and return types correctly when translating
     * between webservice calls and the underlying entity model.
     *
     * @throws Exception
     */
    @Test
    public void testReceive() throws Exception {

        String targetOrg = "targetOrg-HsaId";
        String legalServiceContractType = "riv:etc,etc...";
        String invalidServiceContractType = "invalid";
        String body = "<body name=\"body\" anotherAttribute=\"a value\">the body text<embeddedNode>with some text</embeddedNode></body>";
        String receiverId = "receivingOrgHsaId";
        String replyBody = "OK";

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/ReceiveMessage/" + receiverId);
        when(service.getOkResponseForServiceContract(legalServiceContractType)).thenReturn("<reply>" + replyBody + "</reply>");

        Source request = constructCall(targetOrg, legalServiceContractType, body);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(service);
        impl.setWsContext(wsContext);

        Source resultOfCall = impl.invoke(request);

        ArgumentCaptor<Message> msgArgument = ArgumentCaptor.forClass(Message.class);
        verify(service).saveMessage(msgArgument.capture());

        Message msg = msgArgument.getValue();
        assertEquals(receiverId, msg.getReceiverId());
        assertEquals(legalServiceContractType, msg.getServiceContract());
        assertEquals(targetOrg, msg.getTargetOrganization());
        assertEquals("<?xml version='1.0' encoding='UTF-8'?> <content>" + body + "</content>", msg.getMessageBody());

        //String text = sourceToText(resultOfCall);
        //System.err.println(text);

        DOMResult result = sourceToDom(resultOfCall);
        Document document = (Document) result.getNode();

        Node content = document.getElementsByTagName("reply").item(0);
        assertEquals(replyBody, content.getTextContent());

    }

    @Test
    public void testIllegalServiceContract() throws Exception {
        String targetOrg = "targetOrg-HsaId";
        String invalidServiceContractType = "invalid";
        String body = "<body name=\"body\" anotherAttribute=\"a value\">the body text<embeddedNode>with some text</embeddedNode></body>";
        String receiverId = "receivingOrgHsaId";

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/ReceiveMessage/" + receiverId);

        when(service.getOkResponseForServiceContract(invalidServiceContractType)).thenThrow(new InvalidServiceContractTypeException(invalidServiceContractType));
        Source request = constructCall(targetOrg, invalidServiceContractType, body);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(service);
        impl.setWsContext(wsContext);

        try {
            Source resultOfCall = impl.invoke(request);
            fail("Should get exception");
        } catch (RuntimeException e) {
        }
        verify(service, never()).saveMessage((Message) any());
    }

    @Test
    public void testIllegalXml() throws Exception {
        String targetOrg = "targetOrg-HsaId";
        String legalServiceContractType = "invalid";
        String illegalBody = "<body name=\"body\" anotherAttribute=\"a value>the body text<embeddedNode>with some text</embeddedNode></body>";
        String receiverId = "receivingOrgHsaId";

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/ReceiveMessage/" + receiverId);

        when(service.getOkResponseForServiceContract(legalServiceContractType)).thenReturn("<reply>Ok</reply>");
        Source request = constructCall(targetOrg, legalServiceContractType, illegalBody);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(service);
        impl.setWsContext(wsContext);

        try {
            impl.invoke(request);
            fail("Should get exception");
        } catch (RuntimeException e) {
        }
        verify(service, never()).saveMessage((Message) any());
    }

    @Test
    public void testWrongUrl() throws Exception {
        String targetOrg = "targetOrg-HsaId";
        String legalServiceContractType = "invalid";
        String body = "<body name=\"body\" anotherAttribute=\"a value\">the body text<embeddedNode>with some text</embeddedNode></body>";
        String receiverId = "receivingOrgHsaId";

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/SomeoneRenamedMyEndpointReceiveMessage/" + receiverId);

        when(service.getOkResponseForServiceContract(legalServiceContractType)).thenReturn("<reply>Ok</reply>");
        Source request = constructCall(targetOrg, legalServiceContractType, body);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(service);
        impl.setWsContext(wsContext);

        try {
            impl.invoke(request);
            fail("Should get exception");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().startsWith("Unable to find my own"));
        }
        verify(service, never()).saveMessage((Message) any());
    }
    private DOMResult sourceToDom(Source resultOfCall) throws TransformerException {
        DOMResult result = new DOMResult();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.transform(resultOfCall, result);
        return result;

    }

    private Source constructSoapCallSource(String body, String header, String namespace) {
        String result =
                "<soapenv:Envelope " +
                        "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                        namespace +
                        " >\n" +
                        "    <soapenv:Header/>\n" +
                        "     " + header +
                        "\n   <soapenv:Body>\n" +
                        "      " + body +
                        "\n   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
//        System.err.println("Result\n" + result);
        return new StreamSource(new ByteArrayInputStream(result.getBytes()));
    }

    private Source constructCall(String targetOrg, String tkNamespace, String content) {
        return constructSoapCallSource(
                "<tkns:content>" + content + "</tkns:content>",
                "<urn:LogicalAddress>" + targetOrg + "</urn:LogicalAddress>",
                "xmlns:urn=\"urn:riv:itintegration:registry:1\" xmlns:tkns=\"" + tkNamespace + "\"");
    }

}
