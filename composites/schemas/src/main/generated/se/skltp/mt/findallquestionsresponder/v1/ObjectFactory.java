
package se.skltp.mt.findallquestionsresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.mt.findallquestionsresponder.v1 package. 
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

    private final static QName _FindAllQuestions_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:FindAllQuestionsResponder:1", "FindAllQuestions");
    private final static QName _FindAllQuestionsResponse_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:FindAllQuestionsResponder:1", "FindAllQuestionsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.mt.findallquestionsresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QuestionType }
     * 
     */
    public QuestionType createQuestionType() {
        return new QuestionType();
    }

    /**
     * Create an instance of {@link FindAllQuestionsResponseType }
     * 
     */
    public FindAllQuestionsResponseType createFindAllQuestionsResponseType() {
        return new FindAllQuestionsResponseType();
    }

    /**
     * Create an instance of {@link QuestionsType }
     * 
     */
    public QuestionsType createQuestionsType() {
        return new QuestionsType();
    }

    /**
     * Create an instance of {@link FindAllQuestionsType }
     * 
     */
    public FindAllQuestionsType createFindAllQuestionsType() {
        return new FindAllQuestionsType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAllQuestionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:FindAllQuestionsResponder:1", name = "FindAllQuestions")
    public JAXBElement<FindAllQuestionsType> createFindAllQuestions(FindAllQuestionsType value) {
        return new JAXBElement<FindAllQuestionsType>(_FindAllQuestions_QNAME, FindAllQuestionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FindAllQuestionsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:FindAllQuestionsResponder:1", name = "FindAllQuestionsResponse")
    public JAXBElement<FindAllQuestionsResponseType> createFindAllQuestionsResponse(FindAllQuestionsResponseType value) {
        return new JAXBElement<FindAllQuestionsResponseType>(_FindAllQuestionsResponse_QNAME, FindAllQuestionsResponseType.class, null, value);
    }

}
