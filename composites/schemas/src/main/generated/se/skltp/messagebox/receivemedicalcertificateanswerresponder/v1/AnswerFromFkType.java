
package se.skltp.messagebox.receivemedicalcertificateanswerresponder.v1;

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
import se.skltp.messagebox.qa.v1.Amnetyp;
import se.skltp.messagebox.qa.v1.FkKontaktType;
import se.skltp.messagebox.qa.v1.InnehallType;
import se.skltp.messagebox.qa.v1.LakarutlatandeEnkelType;
import se.skltp.messagebox.qa.v1.VardAdresseringsType;


/**
 * <p>Java class for AnswerFromFkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnswerFromFkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vardReferens-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fkReferens-id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="amne" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}Amnetyp"/>
 *         &lt;element name="fraga" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}innehallType"/>
 *         &lt;element name="svar" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}innehallType"/>
 *         &lt;element name="avsantTidpunkt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fkKontaktInfo" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}fkKontaktType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="adressVard" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}vardAdresseringsType"/>
 *         &lt;element name="lakarutlatande" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}lakarutlatandeEnkelType"/>
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
@XmlType(name = "AnswerFromFkType", propOrder = {
    "vardReferensId",
    "fkReferensId",
    "amne",
    "fraga",
    "svar",
    "avsantTidpunkt",
    "fkKontaktInfo",
    "adressVard",
    "lakarutlatande",
    "any"
})
public class AnswerFromFkType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "vardReferens-id", required = true)
    protected String vardReferensId;
    @XmlElement(name = "fkReferens-id")
    protected String fkReferensId;
    @XmlElement(required = true)
    protected Amnetyp amne;
    @XmlElement(required = true)
    protected InnehallType fraga;
    @XmlElement(required = true)
    protected InnehallType svar;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date avsantTidpunkt;
    protected List<FkKontaktType> fkKontaktInfo;
    @XmlElement(required = true)
    protected VardAdresseringsType adressVard;
    @XmlElement(required = true)
    protected LakarutlatandeEnkelType lakarutlatande;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the vardReferensId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVardReferensId() {
        return vardReferensId;
    }

    /**
     * Sets the value of the vardReferensId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVardReferensId(String value) {
        this.vardReferensId = value;
    }

    /**
     * Gets the value of the fkReferensId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFkReferensId() {
        return fkReferensId;
    }

    /**
     * Sets the value of the fkReferensId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFkReferensId(String value) {
        this.fkReferensId = value;
    }

    /**
     * Gets the value of the amne property.
     * 
     * @return
     *     possible object is
     *     {@link Amnetyp }
     *     
     */
    public Amnetyp getAmne() {
        return amne;
    }

    /**
     * Sets the value of the amne property.
     * 
     * @param value
     *     allowed object is
     *     {@link Amnetyp }
     *     
     */
    public void setAmne(Amnetyp value) {
        this.amne = value;
    }

    /**
     * Gets the value of the fraga property.
     * 
     * @return
     *     possible object is
     *     {@link InnehallType }
     *     
     */
    public InnehallType getFraga() {
        return fraga;
    }

    /**
     * Sets the value of the fraga property.
     * 
     * @param value
     *     allowed object is
     *     {@link InnehallType }
     *     
     */
    public void setFraga(InnehallType value) {
        this.fraga = value;
    }

    /**
     * Gets the value of the svar property.
     * 
     * @return
     *     possible object is
     *     {@link InnehallType }
     *     
     */
    public InnehallType getSvar() {
        return svar;
    }

    /**
     * Sets the value of the svar property.
     * 
     * @param value
     *     allowed object is
     *     {@link InnehallType }
     *     
     */
    public void setSvar(InnehallType value) {
        this.svar = value;
    }

    /**
     * Gets the value of the avsantTidpunkt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getAvsantTidpunkt() {
        return avsantTidpunkt;
    }

    /**
     * Sets the value of the avsantTidpunkt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvsantTidpunkt(Date value) {
        this.avsantTidpunkt = value;
    }

    /**
     * Gets the value of the fkKontaktInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fkKontaktInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFkKontaktInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FkKontaktType }
     * 
     * 
     */
    public List<FkKontaktType> getFkKontaktInfo() {
        if (fkKontaktInfo == null) {
            fkKontaktInfo = new ArrayList<FkKontaktType>();
        }
        return this.fkKontaktInfo;
    }

    /**
     * Gets the value of the adressVard property.
     * 
     * @return
     *     possible object is
     *     {@link VardAdresseringsType }
     *     
     */
    public VardAdresseringsType getAdressVard() {
        return adressVard;
    }

    /**
     * Sets the value of the adressVard property.
     * 
     * @param value
     *     allowed object is
     *     {@link VardAdresseringsType }
     *     
     */
    public void setAdressVard(VardAdresseringsType value) {
        this.adressVard = value;
    }

    /**
     * Gets the value of the lakarutlatande property.
     * 
     * @return
     *     possible object is
     *     {@link LakarutlatandeEnkelType }
     *     
     */
    public LakarutlatandeEnkelType getLakarutlatande() {
        return lakarutlatande;
    }

    /**
     * Sets the value of the lakarutlatande property.
     * 
     * @param value
     *     allowed object is
     *     {@link LakarutlatandeEnkelType }
     *     
     */
    public void setLakarutlatande(LakarutlatandeEnkelType value) {
        this.lakarutlatande = value;
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
