
package iso.v21090.dt.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ANY complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ANY">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:iso:21090:dt:1}HXIT">
 *       &lt;attribute name="nullFlavor" type="{urn:iso:21090:dt:1}NullFlavor" />
 *       &lt;attribute name="flavorId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="updateMode" type="{urn:iso:21090:dt:1}UpdateMode" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ANY")
@XmlSeeAlso({
    II.class,
    CD.class
})
public class ANY
    extends HXIT
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlAttribute(name = "nullFlavor")
    protected NullFlavor nullFlavor;
    @XmlAttribute(name = "flavorId")
    protected String flavorId;
    @XmlAttribute(name = "updateMode")
    protected UpdateMode updateMode;

    /**
     * Gets the value of the nullFlavor property.
     * 
     * @return
     *     possible object is
     *     {@link NullFlavor }
     *     
     */
    public NullFlavor getNullFlavor() {
        return nullFlavor;
    }

    /**
     * Sets the value of the nullFlavor property.
     * 
     * @param value
     *     allowed object is
     *     {@link NullFlavor }
     *     
     */
    public void setNullFlavor(NullFlavor value) {
        this.nullFlavor = value;
    }

    /**
     * Gets the value of the flavorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlavorId() {
        return flavorId;
    }

    /**
     * Sets the value of the flavorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlavorId(String value) {
        this.flavorId = value;
    }

    /**
     * Gets the value of the updateMode property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateMode }
     *     
     */
    public UpdateMode getUpdateMode() {
        return updateMode;
    }

    /**
     * Sets the value of the updateMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateMode }
     *     
     */
    public void setUpdateMode(UpdateMode value) {
        this.updateMode = value;
    }

}
