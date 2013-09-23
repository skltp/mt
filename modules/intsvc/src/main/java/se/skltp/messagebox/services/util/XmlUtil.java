package se.skltp.messagebox.services.util;

import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class XmlUtil {
    private static final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();

    static {
        DBF.setNamespaceAware(true);
    }

    public static String toString(DOMSource source) {
        DOMImplementationLS ls = (DOMImplementationLS) source.getNode().getOwnerDocument().getImplementation();

        LSSerializer serializer = ls.createLSSerializer();
        serializer.getDomConfig().setParameter("xml-declaration", false);
        return serializer.writeToString(source.getNode());
    }

    public static DOMSource fromString(String xml) {
        try {
            DocumentBuilder builder = DBF.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

            return new DOMSource(document.getDocumentElement());
        } catch (Exception e) {
            // typically parsing or configuration exceptions
            throw new RuntimeException(e);
        }
    }
}
