
package se.skltp.mt.receivemedicalcertificateanswerresponder.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.skltp.mt.receivemedicalcertificateanswerresponder.v1 package. 
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

    private final static QName _Answer_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1", "Answer");
    private final static QName _ReceiveMedicalCertificateAnswer_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1", "ReceiveMedicalCertificateAnswer");
    private final static QName _ReceiveMedicalCertificateAnswerResponse_QNAME = new QName("urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1", "ReceiveMedicalCertificateAnswerResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.skltp.mt.receivemedicalcertificateanswerresponder.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReceiveMedicalCertificateAnswerResponseType }
     * 
     */
    public ReceiveMedicalCertificateAnswerResponseType createReceiveMedicalCertificateAnswerResponseType() {
        return new ReceiveMedicalCertificateAnswerResponseType();
    }

    /**
     * Create an instance of {@link ReceiveMedicalCertificateAnswerType }
     * 
     */
    public ReceiveMedicalCertificateAnswerType createReceiveMedicalCertificateAnswerType() {
        return new ReceiveMedicalCertificateAnswerType();
    }

    /**
     * Create an instance of {@link AnswerFromFkType }
     * 
     */
    public AnswerFromFkType createAnswerFromFkType() {
        return new AnswerFromFkType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnswerFromFkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1", name = "Answer")
    public JAXBElement<AnswerFromFkType> createAnswer(AnswerFromFkType value) {
        return new JAXBElement<AnswerFromFkType>(_Answer_QNAME, AnswerFromFkType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveMedicalCertificateAnswerType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1", name = "ReceiveMedicalCertificateAnswer")
    public JAXBElement<ReceiveMedicalCertificateAnswerType> createReceiveMedicalCertificateAnswer(ReceiveMedicalCertificateAnswerType value) {
        return new JAXBElement<ReceiveMedicalCertificateAnswerType>(_ReceiveMedicalCertificateAnswer_QNAME, ReceiveMedicalCertificateAnswerType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiveMedicalCertificateAnswerResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1", name = "ReceiveMedicalCertificateAnswerResponse")
    public JAXBElement<ReceiveMedicalCertificateAnswerResponseType> createReceiveMedicalCertificateAnswerResponse(ReceiveMedicalCertificateAnswerResponseType value) {
        return new JAXBElement<ReceiveMedicalCertificateAnswerResponseType>(_ReceiveMedicalCertificateAnswerResponse_QNAME, ReceiveMedicalCertificateAnswerResponseType.class, null, value);
    }

}
