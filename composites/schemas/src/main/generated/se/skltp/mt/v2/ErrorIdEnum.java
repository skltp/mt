
package se.skltp.mt.v2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorIdEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ErrorIdEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VALIDATION_ERROR"/>
 *     &lt;enumeration value="TRANSFORMATION_ERROR"/>
 *     &lt;enumeration value="APPLICATION_ERROR"/>
 *     &lt;enumeration value="TECHNICAL_ERROR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ErrorIdEnum")
@XmlEnum
public enum ErrorIdEnum {

    VALIDATION_ERROR,
    TRANSFORMATION_ERROR,
    APPLICATION_ERROR,
    TECHNICAL_ERROR;

    public String value() {
        return name();
    }

    public static ErrorIdEnum fromValue(String v) {
        return valueOf(v);
    }

}
