
package se.skltp.riv.itintegration.messagebox.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RECEIVED"/>
 *     &lt;enumeration value="RETRIEVED"/>
 *     &lt;enumeration value="DELETED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageStatusType")
@XmlEnum
public enum MessageStatusType {

    RECEIVED,
    RETRIEVED,
    DELETED;

    public String value() {
        return name();
    }

    public static MessageStatusType fromValue(String v) {
        return valueOf(v);
    }

}
