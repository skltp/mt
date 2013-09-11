
package se.skltp.messagebox.receivemedicalcertificatequestionsponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.messagebox.receivemedicalcertificatequestionsponder.v1 package. 
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

    private final static QName _ReceiveMedicalCertificateQuestionResponse_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1", "ReceiveMedicalCertificateQuestionResponse");
    private final static QName _ReceiveMedicalCertificateQuestion_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1", "ReceiveMedicalCertificateQuestion");
    private final static QName _Question_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1", "Question");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.messagebox.receivemedicalcertificatequestionsponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReceiveMedicalCertificateQuestionType }
     * 
     */
    public ReceiveMedicalCertificateQuestionType createReceiveMedicalCertificateQuestionType() {
        return new ReceiveMedicalCertificateQuestionType();
    }

    /**
     * Create an instance of {@link ReceiveMedicalCertificateQuestionResponseType }
     * 
     */
    public ReceiveMedicalCertificateQuestionResponseType createReceiveMedicalCertificateQuestionResponseType() {
        return new ReceiveMedicalCertificateQuestionResponseType();
    }

    /**
     * Create an instance of {@link QuestionFromFkType }
     * 
     */
    public QuestionFromFkType createQuestionFromFkType() {
        return new QuestionFromFkType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveMedicalCertificateQuestionResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1", name = "ReceiveMedicalCertificateQuestionResponse")
    public JAXBElement<ReceiveMedicalCertificateQuestionResponseType> createReceiveMedicalCertificateQuestionResponse(ReceiveMedicalCertificateQuestionResponseType value) {
        return new JAXBElement<ReceiveMedicalCertificateQuestionResponseType>(_ReceiveMedicalCertificateQuestionResponse_QNAME, ReceiveMedicalCertificateQuestionResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveMedicalCertificateQuestionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1", name = "ReceiveMedicalCertificateQuestion")
    public JAXBElement<ReceiveMedicalCertificateQuestionType> createReceiveMedicalCertificateQuestion(ReceiveMedicalCertificateQuestionType value) {
        return new JAXBElement<ReceiveMedicalCertificateQuestionType>(_ReceiveMedicalCertificateQuestion_QNAME, ReceiveMedicalCertificateQuestionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuestionFromFkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1", name = "Question")
    public JAXBElement<QuestionFromFkType> createQuestion(QuestionFromFkType value) {
        return new JAXBElement<QuestionFromFkType>(_Question_QNAME, QuestionFromFkType.class, null, value);
    }

}
