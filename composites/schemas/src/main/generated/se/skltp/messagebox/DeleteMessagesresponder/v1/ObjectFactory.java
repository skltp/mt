
package se.skltp.messagebox.DeleteMessagesresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.messagebox.DeleteMessagesresponder.v1 package. 
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

    private final static QName _DeleteMessagesResponse_QNAME = new QName("urn:riv:itintegration:messagebox:DeleteMessagesResponder:1", "DeleteMessagesResponse");
    private final static QName _DeleteMessages_QNAME = new QName("urn:riv:itintegration:messagebox:DeleteMessagesResponder:1", "DeleteMessages");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.messagebox.DeleteMessagesresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteMessagesResponseType }
     * 
     */
    public DeleteMessagesResponseType createDeleteMessagesResponseType() {
        return new DeleteMessagesResponseType();
    }

    /**
     * Create an instance of {@link DeleteMessagesType }
     * 
     */
    public DeleteMessagesType createDeleteMessagesType() {
        return new DeleteMessagesType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteMessagesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:messagebox:DeleteMessagesResponder:1", name = "DeleteMessagesResponse")
    public JAXBElement<DeleteMessagesResponseType> createDeleteMessagesResponse(DeleteMessagesResponseType value) {
        return new JAXBElement<DeleteMessagesResponseType>(_DeleteMessagesResponse_QNAME, DeleteMessagesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteMessagesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:itintegration:messagebox:DeleteMessagesResponder:1", name = "DeleteMessages")
    public JAXBElement<DeleteMessagesType> createDeleteMessages(DeleteMessagesType value) {
        return new JAXBElement<DeleteMessagesType>(_DeleteMessages_QNAME, DeleteMessagesType.class, null, value);
    }

}
