package se.skltp.messagebox.services;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.riv.itintegration.messagebox.GenerateUrl.v1.GenerateUrlResponderInterface;
import se.riv.itintegration.messagebox.GenerateUrlResponder.v1.GenerateUrlResponseType;
import se.riv.itintegration.messagebox.GenerateUrlResponder.v1.GenerateUrlType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.skltp.messagebox.core.service.SystemPropertyService;

/**
 * @author mats.olsson@callistaenterprise.se
 */
@WebService(serviceName = "GenerateUrlResponderService",
        endpointInterface = "se.riv.messagebox.GenerateUrl.v1.rivtabp21.GenerateUrlResponderInterface",
        portName = "GenerateUrlResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:GenerateUrl:1:rivtabp21",
        wsdlLocation = "schemas/interactions/GenerateUrlInteraction/GenerateUrlInteraction_1.0_rivtabp21.wsdl")
public class GenerateUrlImpl extends BaseService implements GenerateUrlResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(GenerateUrlImpl.class);

    private SystemPropertyService properties;

    @Autowired
    public void setSystemPropertyService(SystemPropertyService properties) {
        this.properties = properties;
    }


    @Override
    public GenerateUrlResponseType generateUrl(@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress, @WebParam(partName = "parameters", name = "GenerateUrl", targetNamespace = "urn:riv:itintegration:messagebox:GenerateUrlResponder:1") GenerateUrlType parameters) {
        GenerateUrlResponseType response = new GenerateUrlResponseType();

        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        String consumerHsaId = parameters.getConsumerHsaId();
        String encodedHsaId = utf8EncodeUriFragment(consumerHsaId);
        String url = properties.getReceiveMessageUrl().getValue() + "/" + encodedHsaId;
        log.info("Generated url " + url);
        response.setMessageBoxUrl(url);
        return response;
    }

}


