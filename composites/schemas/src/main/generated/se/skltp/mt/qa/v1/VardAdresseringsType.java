
package se.skltp.mt.qa.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;
import se.skltp.mt.v2.HosPersonalType;


/**
 * <p>Java class for vardAdresseringsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vardAdresseringsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hosPersonal" type="{urn:riv:insuranceprocess:healthreporting:2}hosPersonalType"/>
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
@XmlType(name = "vardAdresseringsType", propOrder = {
    "hosPersonal",
    "any"
})
public class VardAdresseringsType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(required = true)
    protected HosPersonalType hosPersonal;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the hosPersonal property.
     * 
     * @return
     *     possible object is
     *     {@link HosPersonalType }
     *     
     */
    public HosPersonalType getHosPersonal() {
        return hosPersonal;
    }

    /**
     * Sets the value of the hosPersonal property.
     * 
     * @param value
     *     allowed object is
     *     {@link HosPersonalType }
     *     
     */
    public void setHosPersonal(HosPersonalType value) {
        this.hosPersonal = value;
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
