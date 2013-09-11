
package se.skltp.mt.deleteanswersresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.mt.deleteanswersresponder.v1 package. 
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

    private final static QName _DeleteAnswers_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1", "DeleteAnswers");
    private final static QName _DeleteAnswersResponse_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1", "DeleteAnswersResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.mt.deleteanswersresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteAnswersType }
     * 
     */
    public DeleteAnswersType createDeleteAnswersType() {
        return new DeleteAnswersType();
    }

    /**
     * Create an instance of {@link DeleteAnswersResponseType }
     * 
     */
    public DeleteAnswersResponseType createDeleteAnswersResponseType() {
        return new DeleteAnswersResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAnswersType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1", name = "DeleteAnswers")
    public JAXBElement<DeleteAnswersType> createDeleteAnswers(DeleteAnswersType value) {
        return new JAXBElement<DeleteAnswersType>(_DeleteAnswers_QNAME, DeleteAnswersType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAnswersResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswersResponder:1", name = "DeleteAnswersResponse")
    public JAXBElement<DeleteAnswersResponseType> createDeleteAnswersResponse(DeleteAnswersResponseType value) {
        return new JAXBElement<DeleteAnswersResponseType>(_DeleteAnswersResponse_QNAME, DeleteAnswersResponseType.class, null, value);
    }

}
