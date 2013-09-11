
package se.skltp.mt.qa.v1;

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


/**
 * <p>Java class for innehallType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="innehallType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="meddelandeText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="signeringsTidpunkt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
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
@XmlType(name = "innehallType", propOrder = {
    "meddelandeText",
    "signeringsTidpunkt",
    "any"
})
public class InnehallType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(required = true)
    protected String meddelandeText;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date signeringsTidpunkt;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the meddelandeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeddelandeText() {
        return meddelandeText;
    }

    /**
     * Sets the value of the meddelandeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeddelandeText(String value) {
        this.meddelandeText = value;
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
