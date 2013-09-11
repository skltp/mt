
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
 * <p>Java class for enhetType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enhetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="enhets-id" type="{urn:iso:21090:dt:1}II"/>
 *         &lt;element name="arbetsplatskod" type="{urn:iso:21090:dt:1}II" minOccurs="0"/>
 *         &lt;element name="enhetsnamn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="postadress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postnummer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefonnummer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="epost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vardgivare" type="{urn:riv:insuranceprocess:healthreporting:2}vardgivareType" minOccurs="0"/>
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
@XmlType(name = "enhetType", propOrder = {
    "enhetsId",
    "arbetsplatskod",
    "enhetsnamn",
    "postadress",
    "postnummer",
    "postort",
    "telefonnummer",
    "epost",
    "vardgivare",
    "any"
})
public class EnhetType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "enhets-id", required = true)
    protected II enhetsId;
    protected II arbetsplatskod;
    @XmlElement(required = true)
    protected String enhetsnamn;
    protected String postadress;
    protected String postnummer;
    protected String postort;
    protected String telefonnummer;
    protected String epost;
    protected VardgivareType vardgivare;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the enhetsId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getEnhetsId() {
        return enhetsId;
    }

    /**
     * Sets the value of the enhetsId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setEnhetsId(II value) {
        this.enhetsId = value;
    }

    /**
     * Gets the value of the arbetsplatskod property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getArbetsplatskod() {
        return arbetsplatskod;
    }

    /**
     * Sets the value of the arbetsplatskod property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setArbetsplatskod(II value) {
        this.arbetsplatskod = value;
    }

    /**
     * Gets the value of the enhetsnamn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnhetsnamn() {
        return enhetsnamn;
    }

    /**
     * Sets the value of the enhetsnamn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnhetsnamn(String value) {
        this.enhetsnamn = value;
    }

    /**
     * Gets the value of the postadress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostadress() {
        return postadress;
    }

    /**
     * Sets the value of the postadress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostadress(String value) {
        this.postadress = value;
    }

    /**
     * Gets the value of the postnummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostnummer() {
        return postnummer;
    }

    /**
     * Sets the value of the postnummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostnummer(String value) {
        this.postnummer = value;
    }

    /**
     * Gets the value of the postort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostort() {
        return postort;
    }

    /**
     * Sets the value of the postort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostort(String value) {
        this.postort = value;
    }

    /**
     * Gets the value of the telefonnummer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefonnummer() {
        return telefonnummer;
    }

    /**
     * Sets the value of the telefonnummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefonnummer(String value) {
        this.telefonnummer = value;
    }

    /**
     * Gets the value of the epost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEpost() {
        return epost;
    }

    /**
     * Sets the value of the epost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEpost(String value) {
        this.epost = value;
    }

    /**
     * Gets the value of the vardgivare property.
     * 
     * @return
     *     possible object is
     *     {@link VardgivareType }
     *     
     */
    public VardgivareType getVardgivare() {
        return vardgivare;
    }

    /**
     * Sets the value of the vardgivare property.
     * 
     * @param value
     *     allowed object is
     *     {@link VardgivareType }
     *     
     */
    public void setVardgivare(VardgivareType value) {
        this.vardgivare = value;
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
