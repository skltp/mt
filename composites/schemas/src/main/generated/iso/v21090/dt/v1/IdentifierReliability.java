
package iso.v21090.dt.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IdentifierReliability.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IdentifierReliability">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ISS"/>
 *     &lt;enumeration value="VER"/>
 *     &lt;enumeration value="UNV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IdentifierReliability")
@XmlEnum
public enum IdentifierReliability {

    ISS,
    VER,
    UNV;

    public String value() {
        return name();
    }

    public static IdentifierReliability fromValue(String v) {
        return valueOf(v);
    }

}
