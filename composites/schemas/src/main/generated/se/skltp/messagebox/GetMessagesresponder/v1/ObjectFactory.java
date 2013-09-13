
package se.skltp.messagebox.GetMessagesresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.messagebox.GetMessagesresponder.v1 package. 
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

    private final static QName _GetMessages_QNAME = new QName("urn:riv:itintegration:messagebox:GetMessagesResponder:1", "GetMessages");
    private final static QName _GetMessagesResponse_QNAME = new QName("urn:riv:itintegration:messagebox:GetMessagesResponder:1", "GetMessagesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.messagebox.GetMessagesresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseType }
     * 
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link GetMessagesResponseType }
     * 
     */
    public GetMessagesResponseType createGetMessagesResponseType() {
        return new GetMessagesResponseType();
    }

    /**
     * Create an instance of {@link GetMessagesType }
     * 
     */
    public GetMessagesType createGetMessagesType() {
        return new GetMessagesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessagesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:messagebox:GetMessagesResponder:1", name = "GetMessages")
    public JAXBElement<GetMessagesType> createGetMessages(GetMessagesType value) {
        return new JAXBElement<GetMessagesType>(_GetMessages_QNAME, GetMessagesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessagesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:messagebox:GetMessagesResponder:1", name = "GetMessagesResponse")
    public JAXBElement<GetMessagesResponseType> createGetMessagesResponse(GetMessagesResponseType value) {
        return new JAXBElement<GetMessagesResponseType>(_GetMessagesResponse_QNAME, GetMessagesResponseType.class, null, value);
    }

}
