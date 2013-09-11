
package org.w3._2001.xmlschema;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (se.skltp.messagebox.schema.jaxb.DateAdapter.parseDate(value));
    }

    public String marshal(Date value) {
        return (se.skltp.messagebox.schema.jaxb.DateAdapter.printDate(value));
    }

}
