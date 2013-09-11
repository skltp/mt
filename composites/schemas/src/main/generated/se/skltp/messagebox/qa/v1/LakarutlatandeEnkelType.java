
package se.skltp.messagebox.qa.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2001.xmlschema.Adapter1;
import org.w3c.dom.Element;
import se.skltp.messagebox.v2.PatientType;


/**
 * <p>Java class for lakarutlatandeEnkelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lakarutlatandeEnkelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lakarutlatande-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signeringsTidpunkt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="patient" type="{urn:riv:insuranceprocess:healthreporting:2}patientType"/>
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
@XmlType(name = "lakarutlatandeEnkelType", propOrder = {
    "lakarutlatandeId",
    "signeringsTidpunkt",
    "patient",
    "any"
})
public class LakarutlatandeEnkelType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "lakarutlatande-id", required = true)
    protected String lakarutlatandeId;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date signeringsTidpunkt;
    @XmlElement(required = true)
    protected PatientType patient;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the lakarutlatandeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLakarutlatandeId() {
        return lakarutlatandeId;
    }

    /**
     * Sets the value of the lakarutlatandeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLakarutlatandeId(String value) {
        this.lakarutlatandeId = value;
    }

    /**
     * Gets the value of the signeringsTidpunkt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getSigneringsTidpunkt() {
        return signeringsTidpunkt;
    }

    /**
     * Sets the value of the signeringsTidpunkt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigneringsTidpunkt(Date value) {
        this.signeringsTidpunkt = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link PatientType }
     *     
     */
    public PatientType getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link PatientType }
     *     
     */
    public void setPatient(PatientType value) {
        this.patient = value;
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
