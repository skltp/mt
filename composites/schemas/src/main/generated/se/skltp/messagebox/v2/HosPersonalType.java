
package se.skltp.messagebox.v2;

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
 * <p>Java class for hosPersonalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hosPersonalType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personal-id" type="{urn:iso:21090:dt:1}II"/>
 *         &lt;element name="fullstandigtNamn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="forskrivarkod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enhet" type="{urn:riv:insuranceprocess:healthreporting:2}enhetType"/>
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
@XmlType(name = "hosPersonalType", propOrder = {
    "personalId",
    "fullstandigtNamn",
    "forskrivarkod",
    "enhet",
    "any"
})
public class HosPersonalType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "personal-id", required = true)
    protected II personalId;
    @XmlElement(required = true)
    protected String fullstandigtNamn;
    protected String forskrivarkod;
    @XmlElement(required = true)
    protected EnhetType enhet;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the personalId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getPersonalId() {
        return personalId;
    }

    /**
     * Sets the value of the personalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setPersonalId(II value) {
        this.personalId = value;
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
     * Gets the value of the forskrivarkod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForskrivarkod() {
        return forskrivarkod;
    }

    /**
     * Sets the value of the forskrivarkod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForskrivarkod(String value) {
        this.forskrivarkod = value;
    }

    /**
     * Gets the value of the enhet property.
     * 
     * @return
     *     possible object is
     *     {@link EnhetType }
     *     
     */
    public EnhetType getEnhet() {
        return enhet;
    }

    /**
     * Sets the value of the enhet property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnhetType }
     *     
     */
    public void setEnhet(EnhetType value) {
        this.enhet = value;
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
