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

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import se.skltp.mb.types.entity.MessageMeta;

/**
 * Generic endpoint capable of handling any service contract.
 * <p/>
 * Extracts message body and metadata and stores it for later List/Get/Delete use.
 */
@ServiceMode(value = Service.Mode.MESSAGE)
@WebServiceProvider(
        /* this is an any-input-service, so we just leave out the wsdl (?)
        targetNamespace = "urn:riv:infrastructure:itintegration:messagebox:ReceiveMessage:1:rivtabp21",
        wsdlLocation = "schemas/interactions/ReceiveMessageInteraction/ReceiveMessageInteraction_1.0_rivtabp21.wsdl",
        */
        serviceName = "ReceiveMessageHttpService",
        portName = "ReceiveMessage"
)
public class ReceiveMessagesImpl extends BaseService implements Provider<SOAPMessage> {

    private static final Logger log = LoggerFactory.getLogger(ReceiveMessagesImpl.class);
    public static final QName TO_QNAME = new QName("http://www.w3.org/2005/08/addressing", "To");
    public static final QName LOGICAL_ADDRESS_QNAME = new QName("urn:riv:itintegration:registry:1", "LogicalAddress");

    @Override
    public SOAPMessage invoke(SOAPMessage soapMessage) {

        String targetSystem = extractTargetSystemFromUrl();
        String callingSystem = extractCallingSystemFromRequest();

        try {

            String sourceSystem = extractCallingSystemFromRequest();
            String correlationId = extractCorrelationIdFromRequest();

            // Get a DOM document from the SOAP body element.
            Document soapBody = XmlUtils.documentFromSoapBody(soapMessage);

            // Extract document type and xml
            String serviceContract = XmlUtils.getFirstElementChild(soapMessage.getSOAPBody()).getNamespaceURI();
            String messageBody = XmlUtils.getDocumentAsString(soapBody);

            String targetOrg = extractTargetOrg(soapMessage);

            MessageMeta message = messageService.create(sourceSystem, targetSystem, targetOrg, serviceContract, messageBody, correlationId);

            if ( log.isInfoEnabled() ) {
                String msgId = message.getId().toString();
                logInfo(getLogger(), "Message " + msgId + " saved by " + callingSystem, msgId, message);
            }

            return getReturnCode();
        } catch (Exception e) {

            if ( log.isErrorEnabled() ) {
                String msg = "Error for ServiceConsumer " + callingSystem + " when trying to send message";
                logError(getLogger(), msg, null, null, e);
            }

            // generate a SOAPFAult with the MT0001 error message in the <faultstring> node
            throw new RuntimeException(ReceiveErrorCodes.MB0001.toString());
        } finally {
            resetLogContext();
        }
    }

    private String extractTargetOrg(SOAPMessage soapMessage) throws SOAPException {
        // rivta2.0 uses To, 2.1 uses LogicalAddress
        Iterator iter = soapMessage.getSOAPHeader().getChildElements(TO_QNAME);
        if ( !iter.hasNext() ) {
            iter = soapMessage.getSOAPHeader().getChildElements(LOGICAL_ADDRESS_QNAME);
        }
        if ( !iter.hasNext() ) {
            throw new RuntimeException("No address node found in header!");
        }
        return ((Node) iter.next()).getValue();
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

    @Override
    public Logger getLogger() {
        return log;
    }

}
