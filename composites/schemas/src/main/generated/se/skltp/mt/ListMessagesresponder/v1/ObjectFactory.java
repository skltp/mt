
package se.skltp.mt.ListMessagesresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.mt.ListMessagesresponder.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ListMessages_QNAME = new QName("urn:riv:itintegration:messagebox:ListMessagesResponder:1", "ListMessages");
    private final static QName _ListMessagesResponse_QNAME = new QName("urn:riv:itintegration:messagebox:ListMessagesResponder:1", "ListMessagesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.mt.ListMessagesresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListMessagesResponseType }
     * 
     */
    public ListMessagesResponseType createListMessagesResponseType() {
        return new ListMessagesResponseType();
    }

    /**
     * Create an instance of {@link ListMessagesType }
     * 
     */
    public ListMessagesType createListMessagesType() {
        return new ListMessagesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListMessagesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:messagebox:ListMessagesResponder:1", name = "ListMessages")
    public JAXBElement<ListMessagesType> createListMessages(ListMessagesType value) {
        return new JAXBElement<ListMessagesType>(_ListMessages_QNAME, ListMessagesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListMessagesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:messagebox:ListMessagesResponder:1", name = "ListMessagesResponse")
    public JAXBElement<ListMessagesResponseType> createListMessagesResponse(ListMessagesResponseType value) {
        return new JAXBElement<ListMessagesResponseType>(_ListMessagesResponse_QNAME, ListMessagesResponseType.class, null, value);
    }

}
