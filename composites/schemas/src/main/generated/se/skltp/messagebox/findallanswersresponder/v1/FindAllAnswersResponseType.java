
package se.skltp.messagebox.findallanswersresponder.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;
import se.skltp.messagebox.v2.ResultOfCall;


/**
 * <p>Java class for FindAllAnswersResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindAllAnswersResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="result" type="{urn:riv:insuranceprocess:healthreporting:2}ResultOfCall" minOccurs="0"/>
 *         &lt;element name="answersLeft" type="{http://www.w3.org/2001/XMLSchema}unsignedShort"/>
 *         &lt;element name="answers" type="{urn:riv:insuranceprocess:healthreporting:FindAllAnswersResponder:1}AnswersType" minOccurs="0"/>
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
@XmlType(name = "FindAllAnswersResponseType", propOrder = {
    "result",
    "answersLeft",
    "answers",
    "any"
})
public class FindAllAnswersResponseType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    protected ResultOfCall result;
    @XmlSchemaType(name = "unsignedShort")
    protected int answersLeft;
    protected AnswersType answers;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link ResultOfCall }
     *     
     */
    public ResultOfCall getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultOfCall }
     *     
     */
    public void setResult(ResultOfCall value) {
        this.result = value;
    }

    /**
     * Gets the value of the answersLeft property.
     * 
     */
    public int getAnswersLeft() {
        return answersLeft;
    }

    /**
     * Sets the value of the answersLeft property.
     * 
     */
    public void setAnswersLeft(int value) {
        this.answersLeft = value;
    }

    /**
     * Gets the value of the answers property.
     * 
     * @return
     *     possible object is
     *     {@link AnswersType }
     *     
     */
    public AnswersType getAnswers() {
        return answers;
    }

    /**
     * Sets the value of the answers property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnswersType }
     *     
     */
    public void setAnswers(AnswersType value) {
        this.answers = value;
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
