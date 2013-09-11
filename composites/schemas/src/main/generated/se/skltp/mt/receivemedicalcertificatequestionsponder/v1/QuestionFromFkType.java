
package se.skltp.mt.receivemedicalcertificatequestionsponder.v1;

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
import org.w3._2001.xmlschema.Adapter2;
import org.w3c.dom.Element;
import se.skltp.mt.qa.v1.Amnetyp;
import se.skltp.mt.qa.v1.FkKontaktType;
import se.skltp.mt.qa.v1.InnehallType;
import se.skltp.mt.qa.v1.KompletteringType;
import se.skltp.mt.qa.v1.LakarutlatandeEnkelType;
import se.skltp.mt.qa.v1.VardAdresseringsType;


/**
 * <p>Java class for QuestionFromFkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionFromFkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fkReferens-id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="amne" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}Amnetyp"/>
 *         &lt;element name="fraga" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}innehallType"/>
 *         &lt;element name="avsantTidpunkt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fkKontaktInfo" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}fkKontaktType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="adressVard" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}vardAdresseringsType"/>
 *         &lt;element name="fkMeddelanderubrik" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fkKomplettering" type="{urn:riv:insuranceprocess:healthreporting:medcertqa:1}kompletteringType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fkSistaDatumForSvar" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
@XmlType(name = "QuestionFromFkType", propOrder = {
    "fkReferensId",
    "amne",
    "fraga",
    "avsantTidpunkt",
    "fkKontaktInfo",
    "adressVard",
    "fkMeddelanderubrik",
    "fkKomplettering",
    "fkSistaDatumForSvar",
    "lakarutlatande",
    "any"
})
public class QuestionFromFkType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "fkReferens-id", required = true)
    protected String fkReferensId;
    @XmlElement(required = true)
    protected Amnetyp amne;
    @XmlElement(required = true)
    protected InnehallType fraga;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date avsantTidpunkt;
    protected List<FkKontaktType> fkKontaktInfo;
    @XmlElement(required = true)
    protected VardAdresseringsType adressVard;
    protected String fkMeddelanderubrik;
    protected List<KompletteringType> fkKomplettering;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Date fkSistaDatumForSvar;
    @XmlElement(required = true)
    protected LakarutlatandeEnkelType lakarutlatande;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

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
     * Gets the value of the fkMeddelanderubrik property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFkMeddelanderubrik() {
        return fkMeddelanderubrik;
    }

    /**
     * Sets the value of the fkMeddelanderubrik property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFkMeddelanderubrik(String value) {
        this.fkMeddelanderubrik = value;
    }

    /**
     * Gets the value of the fkKomplettering property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fkKomplettering property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFkKomplettering().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KompletteringType }
     * 
     * 
     */
    public List<KompletteringType> getFkKomplettering() {
        if (fkKomplettering == null) {
            fkKomplettering = new ArrayList<KompletteringType>();
        }
        return this.fkKomplettering;
    }

    /**
     * Gets the value of the fkSistaDatumForSvar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getFkSistaDatumForSvar() {
        return fkSistaDatumForSvar;
    }

    /**
     * Sets the value of the fkSistaDatumForSvar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFkSistaDatumForSvar(Date value) {
        this.fkSistaDatumForSvar = value;
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
