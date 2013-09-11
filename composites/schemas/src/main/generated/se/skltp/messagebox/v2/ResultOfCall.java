
package se.skltp.messagebox.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * 
 * 				Holds information about the success of the call. If INFO is set the field infoText should contain more information.
 * 				If ERROR is set the fields errorId and errorText will contain information about the error.
 * 			
 * 
 * <p>Java class for ResultOfCall complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultOfCall">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultCode" type="{urn:riv:insuranceprocess:healthreporting:2}ResultCodeEnum"/>
 *         &lt;element name="infoText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorId" type="{urn:riv:insuranceprocess:healthreporting:2}ErrorIdEnum" minOccurs="0"/>
 *         &lt;element name="errorText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "ResultOfCall", propOrder = {
    "resultCode",
    "infoText",
    "errorId",
    "errorText",
    "any"
})
public class ResultOfCall
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(required = true)
    protected ResultCodeEnum resultCode;
    protected String infoText;
    protected ErrorIdEnum errorId;
    protected String errorText;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link ResultCodeEnum }
     *     
     */
    public ResultCodeEnum getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultCodeEnum }
     *     
     */
    public void setResultCode(ResultCodeEnum value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the infoText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfoText() {
        return infoText;
    }

    /**
     * Sets the value of the infoText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfoText(String value) {
        this.infoText = value;
    }

    /**
     * Gets the value of the errorId property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorIdEnum }
     *     
     */
    public ErrorIdEnum getErrorId() {
        return errorId;
    }

    /**
     * Sets the value of the errorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorIdEnum }
     *     
     */
    public void setErrorId(ErrorIdEnum value) {
        this.errorId = value;
    }

    /**
     * Gets the value of the errorText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * Sets the value of the errorText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorText(String value) {
        this.errorText = value;
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
