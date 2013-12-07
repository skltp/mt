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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author mats.olsson@callistaenterprise.se
 */
public class XmlUtils {

    private static DocumentBuilderFactory dbf;
    private static TransformerFactory tf;

    static {
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        tf = TransformerFactory.newInstance();
    }

    public static Document documentFromSoapBody(SOAPMessage soapMessage) throws ParserConfigurationException, SOAPException, TransformerException {
        SOAPBodyElement content = getFirstElementChild(soapMessage.getSOAPBody());
        if (content == null) {
            throw new SOAPException("Unable to find any content in SOAPBody");
        }
        Document document = dbf.newDocumentBuilder().newDocument();
        Node node = document.importNode(content, true);
        document.appendChild(node);
        return document;
    }

    // need to skip any text elements to find the first actual content node

    /**
     * Return the first SOAPBodyElement find in the children of the node.
     *
     * @param node whose children to scan
     * @return first found SOAPBodyElement node or null
     */
    public static SOAPBodyElement getFirstElementChild(SOAPElement node) {
        Iterator children = node.getChildElements();
        SOAPBodyElement result = null;
        while ( children.hasNext() ) {
            Object child = children.next();
            if ( child instanceof SOAPBodyElement ) {
                result = (SOAPBodyElement) child;
                break;
            }
        }
        return result;
    }

    public static String getDocumentAsString(Document doc) throws TransformerException {
        StringWriter sw = new StringWriter();
        tf.newTransformer().transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
    }

    public static DOMResult getStringAsDom(String text) throws TransformerException {
        Source txtSrc = new StreamSource(new StringReader(text));
        DOMResult result = new DOMResult();
        tf.newTransformer().transform(txtSrc, result);
        return result;
    }



    /**
     * Format the xml-text string in a human-readable fashion.
     *
     * @param text xml-document text
     * @return formatted xml-document
     * @throws TransformerException
     */
    public static String prettyFormatXmlText(String text) throws TransformerException {
        // Instantiate transformer input
        Source xmlInput = new StreamSource(new StringReader(text));
        StreamResult xmlOutput = new StreamResult(new StringWriter());

        // Configure transformer
        Transformer transformer = TransformerFactory.newInstance().newTransformer(); // An identity transformer
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(xmlInput, xmlOutput);

        return xmlOutput.getWriter().toString();
    }
}
