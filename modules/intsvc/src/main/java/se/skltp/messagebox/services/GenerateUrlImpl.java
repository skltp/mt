/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera MessageService (http://code.google.com/p/inera-message).
 *
 * Inera MessageService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera MessageService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        endpointInterface = "se.riv.itintegration.messagebox.GenerateUrl.v1.GenerateUrlResponderInterface",
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
    
    @Override
    public Logger getLogger() {
        return log;
    }
    

}


