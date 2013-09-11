
package se.skltp.messagebox.findallanswersresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.messagebox.findallanswersresponder.v1 package. 
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

    private final static QName _FindAllAnswersResponse_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:FindAllAnswersResponder:1", "FindAllAnswersResponse");
    private final static QName _FindAllAnswers_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:FindAllAnswersResponder:1", "FindAllAnswers");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.messagebox.findallanswersresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FindAllAnswersType }
     * 
     */
    public FindAllAnswersType createFindAllAnswersType() {
        return new FindAllAnswersType();
    }

    /**
     * Create an instance of {@link AnswerType }
     * 
     */
    public AnswerType createAnswerType() {
        return new AnswerType();
    }

    /**
     * Create an instance of {@link AnswersType }
     * 
     */
    public AnswersType createAnswersType() {
        return new AnswersType();
    }

    /**
     * Create an instance of {@link FindAllAnswersResponseType }
     * 
     */
    public FindAllAnswersResponseType createFindAllAnswersResponseType() {
        return new FindAllAnswersResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAllAnswersResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:FindAllAnswersResponder:1", name = "FindAllAnswersResponse")
    public JAXBElement<FindAllAnswersResponseType> createFindAllAnswersResponse(FindAllAnswersResponseType value) {
        return new JAXBElement<FindAllAnswersResponseType>(_FindAllAnswersResponse_QNAME, FindAllAnswersResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAllAnswersType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:FindAllAnswersResponder:1", name = "FindAllAnswers")
    public JAXBElement<FindAllAnswersType> createFindAllAnswers(FindAllAnswersType value) {
        return new JAXBElement<FindAllAnswersType>(_FindAllAnswers_QNAME, FindAllAnswersType.class, null, value);
    }

}
