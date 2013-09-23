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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        String serviceContractType = "riv:etc,etc...";
        String body = "the body text";
        String receiverId = "receivingOrgHsaId";
        String replyBody = "OK";

        when(wsContext.getMessageContext()).thenReturn(msgContext);
        when(msgContext.get(MessageContext.SERVLET_REQUEST)).thenReturn(servletRequest);
        when(servletRequest.getRequestURI()).thenReturn("/ReceiveMessage/" + receiverId);
        when(service.getOkResponseForServiceContract(serviceContractType)).thenReturn("<reply>" + replyBody + "</reply>");

        Source request = constructCall(targetOrg, serviceContractType, body);

        ReceiveMessagesImpl impl = new ReceiveMessagesImpl();
        impl.setMessageService(service);
        impl.setWsContext(wsContext);

        Source resultOfCall = impl.invoke(request);

        ArgumentCaptor<Message> msgArgument = ArgumentCaptor.forClass(Message.class);
        verify(service).saveMessage(msgArgument.capture());

        Message msg = msgArgument.getValue();
        assertEquals(receiverId, msg.getReceiverId());
        assertEquals(serviceContractType, msg.getServiceContract());
        assertEquals(targetOrg, msg.getTargetOrganization());
        assertEquals("<?xml version='1.0' encoding='UTF-8'?> <content>" + body + "</content>", msg.getMessageBody());

        //String text = sourceToText(resultOfCall);
        //System.err.println(text);

        DOMResult result = sourceToDom(resultOfCall);
        Document document = (Document) result.getNode();

        Node content = document.getElementsByTagName("reply").item(0);
        assertEquals(replyBody, content.getTextContent());
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
