
/*
 * 
 */

package se.skltp.messagebox.findallquestions.v1.rivtabp20;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.3.0
 * Wed Sep 11 11:15:56 CEST 2013
 * Generated source version: 2.3.0
 * 
 */


@WebServiceClient(name = "FindAllQuestionsResponderService", 
                  wsdlLocation = "file:/Users/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/FindAllQuestionsInteraction/FindAllQuestionsInteraction_1.0_rivtabp20.wsdl",
                  targetNamespace = "urn:riv:insuranceprocess:healthreporting:FindAllQuestions:1:rivtabp20") 
public class FindAllQuestionsResponderService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("urn:riv:insuranceprocess:healthreporting:FindAllQuestions:1:rivtabp20", "FindAllQuestionsResponderService");
    public final static QName FindAllQuestionsResponderPort = new QName("urn:riv:insuranceprocess:healthreporting:FindAllQuestions:1:rivtabp20", "FindAllQuestionsResponderPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/Users/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/FindAllQuestionsInteraction/FindAllQuestionsInteraction_1.0_rivtabp20.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/Users/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/FindAllQuestionsInteraction/FindAllQuestionsInteraction_1.0_rivtabp20.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public FindAllQuestionsResponderService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FindAllQuestionsResponderService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FindAllQuestionsResponderService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public FindAllQuestionsResponderService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }
    public FindAllQuestionsResponderService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public FindAllQuestionsResponderService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns FindAllQuestionsResponderInterface
     */
    @WebEndpoint(name = "FindAllQuestionsResponderPort")
    public FindAllQuestionsResponderInterface getFindAllQuestionsResponderPort() {
        return super.getPort(FindAllQuestionsResponderPort, FindAllQuestionsResponderInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns FindAllQuestionsResponderInterface
     */
    @WebEndpoint(name = "FindAllQuestionsResponderPort")
    public FindAllQuestionsResponderInterface getFindAllQuestionsResponderPort(WebServiceFeature... features) {
        return super.getPort(FindAllQuestionsResponderPort, FindAllQuestionsResponderInterface.class, features);
    }

}
