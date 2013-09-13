
/*
 * 
 */

package se.skltp.messagebox.GetMessages.v1.rivtabp21;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.3.0
 * Thu Sep 12 16:56:08 CEST 2013
 * Generated source version: 2.3.0
 * 
 */


@WebServiceClient(name = "GetMessagesResponderService", 
                  wsdlLocation = "file:/Volumes/MacCaseSensitive/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/GetMessagesInteraction/GetMessagesInteraction_1.0_rivtabp21.wsdl",
                  targetNamespace = "urn:riv:itintegration:messagebox:GetMessages:1:rivtabp21") 
public class GetMessagesResponderService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("urn:riv:itintegration:messagebox:GetMessages:1:rivtabp21", "GetMessagesResponderService");
    public final static QName GetMessagesResponderPort = new QName("urn:riv:itintegration:messagebox:GetMessages:1:rivtabp21", "GetMessagesResponderPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/Volumes/MacCaseSensitive/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/GetMessagesInteraction/GetMessagesInteraction_1.0_rivtabp21.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/Volumes/MacCaseSensitive/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/GetMessagesInteraction/GetMessagesInteraction_1.0_rivtabp21.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public GetMessagesResponderService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public GetMessagesResponderService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public GetMessagesResponderService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public GetMessagesResponderService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }
    public GetMessagesResponderService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public GetMessagesResponderService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns GetMessagesResponderInterface
     */
    @WebEndpoint(name = "GetMessagesResponderPort")
    public GetMessagesResponderInterface getGetMessagesResponderPort() {
        return super.getPort(GetMessagesResponderPort, GetMessagesResponderInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns GetMessagesResponderInterface
     */
    @WebEndpoint(name = "GetMessagesResponderPort")
    public GetMessagesResponderInterface getGetMessagesResponderPort(WebServiceFeature... features) {
        return super.getPort(GetMessagesResponderPort, GetMessagesResponderInterface.class, features);
    }

}
