
package se.skltp.mt.receivemedicalcertificatequestionsponder.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for ReceiveMedicalCertificateQuestionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReceiveMedicalCertificateQuestionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestionResponder:1}Question"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReceiveMedicalCertificateQuestionType", propOrder = {
    "question",
    "any"
})
public class ReceiveMedicalCertificateQuestionType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "Question", required = true)
    protected QuestionFromFkType question;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the question property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionFromFkType }
     *     
     */
    public QuestionFromFkType getQuestion() {
        return question;
    }

    /**
     * Sets the value of the question property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionFromFkType }
     *     
     */
    public void setQuestion(QuestionFromFkType value) {
        this.question = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}