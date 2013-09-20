package se.skltp.messagebox.services;

import java.io.UnsupportedEncodingException;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;
import se.skltp.messagebox.GenerateUrl.v1.rivtabp21.GenerateUrlResponderInterface;
import se.skltp.messagebox.GenerateUrlresponder.v1.GenerateUrlResponseType;
import se.skltp.messagebox.GenerateUrlresponder.v1.GenerateUrlType;
import se.skltp.messagebox.core.service.SystemPropertyService;
import se.skltp.riv.itintegration.messagebox.v1.ResultCodeEnum;

/**
 * @author mats.olsson@callistaenterprise.se
 */


@WebService(serviceName = "GenerateUrlResponderService",
        endpointInterface = "se.skltp.messagebox.GenerateUrl.v1.rivtabp21.GenerateUrlResponderInterface",
        portName = "GenerateUrlResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:GenerateUrl:1:rivtabp21",
        wsdlLocation = "schemas/interactions/GenerateUrlInteraction/GenerateUrlInteraction_1.0_rivtabp21.wsdl")
public class GenerateUrlImpl extends BaseService implements GenerateUrlResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(GenerateUrlImpl.class);

    @Autowired
    SystemPropertyService properties;

    @Override
    public GenerateUrlResponseType generateUrl(@WebParam(partName = "LogicalAddress", name = "LogicalAddress", targetNamespace = "urn:riv:itintegration:registry:1", header = true) String logicalAddress, @WebParam(partName = "parameters", name = "GenerateUrl", targetNamespace = "urn:riv:itintegration:messagebox:GenerateUrlResponder:1") GenerateUrlType parameters) {
        GenerateUrlResponseType response = new GenerateUrlResponseType();
        response.setResultCode(ResultCodeEnum.OK);

        String consumerHsaId = parameters.getConsumerHsaId();
        try {
            String encodedHsaId = UriUtils.encodeFragment(consumerHsaId, "utf-8");
            String url = properties.getReceiveMessageUrl().getValue() + "/" + encodedHsaId;
            log.info("Generated url " + url);
            response.setMessageBoxUrl(url);
            return response;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e); // should never happen, tied to "utf-8"
        }


    }
}


