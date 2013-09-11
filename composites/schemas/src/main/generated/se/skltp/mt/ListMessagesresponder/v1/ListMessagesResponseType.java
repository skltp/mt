
package se.skltp.mt.ListMessagesresponder.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;
import se.skltp.riv.itintegration.messagebox.v1.MessageMetaType;


/**
 * <p>Java class for ListMessagesResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListMessagesResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageMetas" type="{urn:riv:itintegration:messagebox:1}MessageMetaType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ResultCode" type="{urn:riv:itintegration:messagebox:ListMessagesResponder:1}ResultCodeEnum"/>
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
@XmlType(name = "ListMessagesResponseType", propOrder = {
    "messageMetas",
    "resultCode",
    "any"
})
public class ListMessagesResponseType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    protected List<MessageMetaType> messageMetas;
    @XmlElement(name = "ResultCode", required = true)
    protected ResultCodeEnum resultCode;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the messageMetas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageMetas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageMetas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageMetaType }
     * 
     * 
     */
    public List<MessageMetaType> getMessageMetas() {
        if (messageMetas == null) {
            messageMetas = new ArrayList<MessageMetaType>();
        }
        return this.messageMetas;
    }

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
