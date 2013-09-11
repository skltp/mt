
package se.skltp.messagebox.deletequestionsresponder.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import iso.v21090.dt.v1.II;
import org.w3c.dom.Element;


/**
 * <p>Java class for DeleteQuestionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteQuestionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="careUnitId" type="{urn:iso:21090:dt:1}II"/>
 *         &lt;element name="careGiverId" type="{urn:iso:21090:dt:1}II"/>
 *         &lt;element name="questionId" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
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
@XmlType(name = "DeleteQuestionsType", propOrder = {
    "careUnitId",
    "careGiverId",
    "questionId",
    "any"
})
public class DeleteQuestionsType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(required = true)
    protected II careUnitId;
    @XmlElement(required = true)
    protected II careGiverId;
    @XmlElement(required = true)
    protected List<String> questionId;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the careUnitId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getCareUnitId() {
        return careUnitId;
    }

    /**
     * Sets the value of the careUnitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setCareUnitId(II value) {
        this.careUnitId = value;
    }

    /**
     * Gets the value of the careGiverId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getCareGiverId() {
        return careGiverId;
    }

    /**
     * Sets the value of the careGiverId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setCareGiverId(II value) {
        this.careGiverId = value;
    }

    /**
     * Gets the value of the questionId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the questionId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestionId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getQuestionId() {
        if (questionId == null) {
            questionId = new ArrayList<String>();
        }
        return this.questionId;
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
