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
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.service.MessageService;

@ServiceMode(value = Service.Mode.MESSAGE)
@WebServiceProvider(
        /* this is an any-input-service, so we just leave out the wsdl (?)
        targetNamespace = "urn:riv:itintegration:messagebox:ReceiveMessage:1:rivtabp21",
        wsdlLocation = "schemas/interactions/ReceiveMessageInteraction/ReceiveMessageInteraction_1.0_rivtabp21.wsdl",
        */
        serviceName = "ReceiveMessageHttpService",
        portName = "ReceiveMessage"
)
public class ReceiveMessagesImpl implements Provider<Source> {

    private static final Logger log = LoggerFactory.getLogger(ReceiveMessagesImpl.class);

    private MessageService messageService;

    @Resource
    public void setMessageService(MessageService MessageService) {
        this.messageService = MessageService;
    }

    @Override
    public Source invoke(Source request) {
        try {

            if ( request != null ) {
                // first translate the body into a string
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                Extractor extractor = new Extractor();
                trans.transform(request, new SAXResult(extractor));

                String systemId = extractor.getSystemId();
                String serviceContract = extractor.getServiceContract();
                String messageBody = extractor.getBody();

                Message message = new Message(systemId, serviceContract, messageBody);
                messageService.saveMessage(message);
                log.info("Saved " + message);
                return sendSource("Ok");
            } else {
                return sendSource("Ok (null)");
            }

        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private Source sendSource(String input) {
        // TODO: manually(?) construct the required reply
        String body = "<reply>" + input + "</reply>";
        String header = "";
        String result =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" >\n" +
                        "   <soapenv:Header/>\n" +
                        header +
                        "   <soapenv:Body>\n" +
                        body +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return new StreamSource(new ByteArrayInputStream(result.getBytes()));
    }

    /**
     * Parses the soap envelope and extracts required data.
     * 
     * <ol>
     * <li>The logical address of the receiver</li>
     * <li>The service contract (identified by the namespace of the first element in body)</li>
     * <li>The actual body of the envelope</li>
     * </ol>
     */
    private class Extractor extends DefaultHandler {

        // namespaces used
        private static final String SOAP_NS = "http://schemas.xmlsoap.org/soap/envelope/";
        private static final String REG_NS = "urn:riv:itintegration:registry:1";

        private String serviceContract = null;
        private boolean inBody = false;
        private boolean inLogicalAddress = false;

        private StringBuilder bodyBuilder = new StringBuilder();
        private StringBuilder logicalAddressBuilder = new StringBuilder();
        private StringBuilder textBuilder = new StringBuilder();


        public String getServiceContract() {
            return serviceContract;
        }

        public String getBody() {
            // note: we trim the whitespace around the body element
            return "<?xml version='1.0' encoding='UTF-8'?> " + bodyBuilder.toString().trim();
        }

        /**
         * Handle the start of an xml-node.
         *
         * @param uri   namespaceURI
         * @param sName simple
         * @param qName qualified
         * @param attrs attributes
         * @throws SAXException
         */
        public void startElement(String uri, String sName, String qName, Attributes attrs) throws SAXException {
            // use qualified if simple name is empty (non-namespace aware stuff)
            String eName = sName.equals("") ? qName : sName;

            flushText();
            // grab the namespace of the first element after the soap-body is found
            if ( inBody && serviceContract == null ) {
                serviceContract = uri;
//                log.info("serviceContract: " + serviceContract);
            }

            if ( inBody ) {
                bodyBuilder.append("<").append(eName);
                if ( attrs != null ) {
                    for ( int i = 0; i < attrs.getLength(); i++ ) {
                        String aName = attrs.getLocalName(i);
                        if ( "".equals(aName) ) aName = attrs.getQName(i);
                        bodyBuilder.append(" ").append(aName);
                        bodyBuilder.append("=\"").append(attrs.getValue(i)).append("\"");
                    }
                }
                bodyBuilder.append(">");
            } else {
                if ( eName.equalsIgnoreCase("body") && uri.equals(SOAP_NS) ) {
                    inBody = true;
                }
                if ( eName.equalsIgnoreCase("logicaladdress") && uri.equals(REG_NS) ) {
                    inLogicalAddress = true;
                }
            }
        }

        public void endElement(String uri, String sName, String qName) throws SAXException {
            String eName = sName.equals("") ? qName : sName;
            flushText();
            if ( eName.equalsIgnoreCase("body") && uri.equals(SOAP_NS) ) {
                inBody = false;
                bodyBuilder.append("\n");
//                log.debug("body:\n" + bodyBuilder.toString());
            }
            if ( inBody ) {
                bodyBuilder.append("</").append(eName).append(">");
            }
            if ( eName.equalsIgnoreCase("logicaladdress") && uri.equals(REG_NS) ) {
                inLogicalAddress = false;
//                log.info("logical address: '" + logicalAddressBuilder.toString() + "'");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            textBuilder.append(ch,start,length);

        }

        // flush any text accumulated between nodes
        private void flushText() {
            String txt = textBuilder.toString();
            textBuilder.replace(0, textBuilder.length(), "");
            if ( inLogicalAddress ) {
                // trim any whitespace around the logical address
                logicalAddressBuilder.append(txt.trim());
            }
            if ( inBody ) {
                // keep whitespace in body
                bodyBuilder.append(txt);
            }
        }

        public String getSystemId() {
            return logicalAddressBuilder.toString();
        }
    }
}