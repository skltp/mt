package se.skltp.messagebox.services;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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

    public static Document documentFromSoapBody(SOAPMessage soapMessage) throws ParserConfigurationException, SOAPException {
        SOAPBodyElement e = (SOAPBodyElement)soapMessage.getSOAPBody().getChildElements().next();
        Document document = dbf.newDocumentBuilder().newDocument();
        Node node = document.importNode(e, true);
        document.appendChild(node);
        return document;
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
}
