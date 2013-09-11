
/*
 * 
 */

package se.skltp.messagebox.deletequestions.v1.rivtabp20;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.3.0
 * Wed Sep 11 11:15:58 CEST 2013
 * Generated source version: 2.3.0
 * 
 */


@WebServiceClient(name = "DeleteQuestionsResponderService", 
                  wsdlLocation = "file:/Users/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/DeleteQuestionsInteraction/DeleteQuestionsInteraction_1.0_rivtabp20.wsdl",
                  targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteQuestions:1:rivtabp20") 
public class DeleteQuestionsResponderService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("urn:riv:insuranceprocess:healthreporting:DeleteQuestions:1:rivtabp20", "DeleteQuestionsResponderService");
    public final static QName DeleteQuestionsResponderPort = new QName("urn:riv:insuranceprocess:healthreporting:DeleteQuestions:1:rivtabp20", "DeleteQuestionsResponderPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/Users/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/DeleteQuestionsInteraction/DeleteQuestionsInteraction_1.0_rivtabp20.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:/Users/matsolsson/Callista/inera-message/composites/schemas/src/main/resources/schemas/interactions/DeleteQuestionsInteraction/DeleteQuestionsInteraction_1.0_rivtabp20.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public DeleteQuestionsResponderService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public DeleteQuestionsResponderService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DeleteQuestionsResponderService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public DeleteQuestionsResponderService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }
    public DeleteQuestionsResponderService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public DeleteQuestionsResponderService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns DeleteQuestionsResponderInterface
     */
    @WebEndpoint(name = "DeleteQuestionsResponderPort")
    public DeleteQuestionsResponderInterface getDeleteQuestionsResponderPort() {
        return super.getPort(DeleteQuestionsResponderPort, DeleteQuestionsResponderInterface.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DeleteQuestionsResponderInterface
     */
    @WebEndpoint(name = "DeleteQuestionsResponderPort")
    public DeleteQuestionsResponderInterface getDeleteQuestionsResponderPort(WebServiceFeature... features) {
        return super.getPort(DeleteQuestionsResponderPort, DeleteQuestionsResponderInterface.class, features);
    }

}