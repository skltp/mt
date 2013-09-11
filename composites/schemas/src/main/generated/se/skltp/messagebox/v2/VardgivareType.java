
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
 * <p>Java class for vardgivareType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vardgivareType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vardgivare-id" type="{urn:iso:21090:dt:1}II"/>
 *         &lt;element name="vardgivarnamn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "vardgivareType", propOrder = {
    "vardgivareId",
    "vardgivarnamn",
    "any"
})
public class VardgivareType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "vardgivare-id", required = true)
    protected II vardgivareId;
    @XmlElement(required = true)
    protected String vardgivarnamn;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the vardgivareId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getVardgivareId() {
        return vardgivareId;
    }

    /**
     * Sets the value of the vardgivareId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setVardgivareId(II value) {
        this.vardgivareId = value;
    }

    /**
     * Gets the value of the vardgivarnamn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVardgivarnamn() {
        return vardgivarnamn;
    }

    /**
     * Sets the value of the vardgivarnamn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVardgivarnamn(String value) {
        this.vardgivarnamn = value;
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
