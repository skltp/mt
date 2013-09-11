
package se.skltp.mt.deletequestionsresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.mt.deletequestionsresponder.v1 package. 
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

    private final static QName _DeleteQuestions_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:DeleteQuestionsResponder:1", "DeleteQuestions");
    private final static QName _DeleteQuestionsResponse_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:DeleteQuestionsResponder:1", "DeleteQuestionsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.mt.deletequestionsresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DeleteQuestionsType }
     * 
     */
    public DeleteQuestionsType createDeleteQuestionsType() {
        return new DeleteQuestionsType();
    }

    /**
     * Create an instance of {@link DeleteQuestionsResponseType }
     * 
     */
    public DeleteQuestionsResponseType createDeleteQuestionsResponseType() {
        return new DeleteQuestionsResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteQuestionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:DeleteQuestionsResponder:1", name = "DeleteQuestions")
    public JAXBElement<DeleteQuestionsType> createDeleteQuestions(DeleteQuestionsType value) {
        return new JAXBElement<DeleteQuestionsType>(_DeleteQuestions_QNAME, DeleteQuestionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteQuestionsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:DeleteQuestionsResponder:1", name = "DeleteQuestionsResponse")
    public JAXBElement<DeleteQuestionsResponseType> createDeleteQuestionsResponse(DeleteQuestionsResponseType value) {
        return new JAXBElement<DeleteQuestionsResponseType>(_DeleteQuestionsResponse_QNAME, DeleteQuestionsResponseType.class, null, value);
    }

}
