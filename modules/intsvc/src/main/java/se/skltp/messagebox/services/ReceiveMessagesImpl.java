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

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import se.skltp.messagebox.core.entity.Message;

@ServiceMode(value = Service.Mode.MESSAGE)
@WebServiceProvider(
        /* this is an any-input-service, so we just leave out the wsdl (?)
        targetNamespace = "urn:riv:itintegration:messagebox:ReceiveMessage:1:rivtabp21",
        wsdlLocation = "schemas/interactions/ReceiveMessageInteraction/ReceiveMessageInteraction_1.0_rivtabp21.wsdl",
        */
        serviceName = "ReceiveMessageHttpService",
        portName = "ReceiveMessage"
)
public class ReceiveMessagesImpl extends BaseService implements Provider<SOAPMessage> {

    private static final Logger log = LoggerFactory.getLogger(ReceiveMessagesImpl.class);
    private static final String ENDPOINT_NAME = "/ReceiveMessage/";
    public static final QName LOGICAL_ADDRESS_QNAME = new QName("urn:riv:itintegration:registry:1", "LogicalAddress");

    @Override
    public SOAPMessage invoke(SOAPMessage soapMessage) {
        try {
            String targetSystem = extractReceivingSystemHsaId();
            String sourceSystem = extractCallerIdFromRequest();

            // Get a DOM document from the SOAP body element.
            Document soapBody = XmlUtils.documentFromSoapBody(soapMessage);

            // Extract document type and xml
            String serviceContract = soapBody.getChildNodes().item(0).getNamespaceURI();
            String messageBody = XmlUtils.getDocumentAsString(soapBody);

            Node logicalAddressNode = (Node) soapMessage.getSOAPHeader().getChildElements(LOGICAL_ADDRESS_QNAME).next();
            String targetOrg = logicalAddressNode.getValue();

            Message message = messageService.create(sourceSystem, targetSystem, targetOrg, serviceContract, messageBody);

            log.debug("Saved " + message);

            return getReturnCode();
            // TODO: translate to correct SOAPFault errors!
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SOAPException e) {
            throw new RuntimeException(e);

        }
    }

    // the hsa-id is the part of the url after the "ReceiveMessage/"
    private String extractReceivingSystemHsaId() {
        MessageContext ctx = wsContext.getMessageContext();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.get(MessageContext.SERVLET_REQUEST);
        String uri = servletRequest.getRequestURI();
        int n = uri.indexOf(ENDPOINT_NAME);
        if ( n == -1 ) {
            throw new RuntimeException("Unable to find my own endpoint name \"" + ENDPOINT_NAME + "\" in uri \"" + uri + "\"");
        }
        String encodedHsaId = uri.substring(n + ENDPOINT_NAME.length());
        return uf8DecodeUri(encodedHsaId);
    }

    /**
     * The response is ALWAYS completely empty - there is no way to communicate back to the
     * originator in any way as they should not be aware that we exist.
     *
     * @return an empty SOAPMessage
     * @throws SOAPException
     */
    SOAPMessage getReturnCode() throws SOAPException {
        MessageFactory factory = MessageFactory.newInstance();
        return factory.createMessage();
    }

}
