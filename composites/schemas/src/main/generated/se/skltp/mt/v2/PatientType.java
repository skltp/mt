
package se.skltp.mt.v2;

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
 * <p>Java class for patientType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="patientType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="person-id" type="{urn:iso:21090:dt:1}II"/>
 *         &lt;element name="fullstandigtNamn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "patientType", propOrder = {
    "personId",
    "fullstandigtNamn",
    "any"
})
public class PatientType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "person-id", required = true)
    protected II personId;
    @XmlElement(required = true)
    protected String fullstandigtNamn;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the personId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getPersonId() {
        return personId;
    }

    /**
     * Sets the value of the personId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setPersonId(II value) {
        this.personId = value;
    }

    /**
     * Gets the value of the fullstandigtNamn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullstandigtNamn() {
        return fullstandigtNamn;
    }

    /**
     * Sets the value of the fullstandigtNamn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullstandigtNamn(String value) {
        this.fullstandigtNamn = value;
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
