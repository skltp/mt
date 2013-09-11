
package se.skltp.messagebox.receivemedicalcertificateanswerresponder.v1;

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
 * <p>Java class for ReceiveMedicalCertificateAnswerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReceiveMedicalCertificateAnswerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswerResponder:1}Answer"/>
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
@XmlType(name = "ReceiveMedicalCertificateAnswerType", propOrder = {
    "answer",
    "any"
})
public class ReceiveMedicalCertificateAnswerType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "Answer", required = true)
    protected AnswerFromFkType answer;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the answer property.
     * 
     * @return
     *     possible object is
     *     {@link AnswerFromFkType }
     *     
     */
    public AnswerFromFkType getAnswer() {
        return answer;
    }

    /**
     * Sets the value of the answer property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnswerFromFkType }
     *     
     */
    public void setAnswer(AnswerFromFkType value) {
        this.answer = value;
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
