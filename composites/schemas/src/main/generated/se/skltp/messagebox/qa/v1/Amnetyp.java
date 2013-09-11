
package se.skltp.messagebox.qa.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Amnetyp.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Amnetyp">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Komplettering_av_lakarintyg"/>
 *     &lt;enumeration value="Makulering_av_lakarintyg"/>
 *     &lt;enumeration value="Avstamningsmote"/>
 *     &lt;enumeration value="Kontakt"/>
 *     &lt;enumeration value="Arbetstidsforlaggning"/>
 *     &lt;enumeration value="Paminnelse"/>
 *     &lt;enumeration value="Ovrigt"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Amnetyp")
@XmlEnum
public enum Amnetyp {

    @XmlEnumValue("Komplettering_av_lakarintyg")
    KOMPLETTERING_AV_LAKARINTYG("Komplettering_av_lakarintyg"),
    @XmlEnumValue("Makulering_av_lakarintyg")
    MAKULERING_AV_LAKARINTYG("Makulering_av_lakarintyg"),
    @XmlEnumValue("Avstamningsmote")
    AVSTAMNINGSMOTE("Avstamningsmote"),
    @XmlEnumValue("Kontakt")
    KONTAKT("Kontakt"),
    @XmlEnumValue("Arbetstidsforlaggning")
    ARBETSTIDSFORLAGGNING("Arbetstidsforlaggning"),
    @XmlEnumValue("Paminnelse")
    PAMINNELSE("Paminnelse"),
    @XmlEnumValue("Ovrigt")
    OVRIGT("Ovrigt");
    private final String value;

    Amnetyp(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Amnetyp fromValue(String v) {
        for (Amnetyp c: Amnetyp.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
