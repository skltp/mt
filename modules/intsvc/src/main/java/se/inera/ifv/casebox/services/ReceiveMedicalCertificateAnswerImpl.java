/*
 * Copyright 2010 Inera
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *
 *   Boston, MA 02111-1307  USA
 */
package se.inera.ifv.casebox.services;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.ifv.casebox.core.entity.Answer;
import se.inera.ifv.casebox.core.service.AnswerService;
import se.inera.ifv.receivemedicalcertificateanswer.v1.rivtabp20.ReceiveMedicalCertificateAnswerResponderInterface;
import se.inera.ifv.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerResponseType;
import se.inera.ifv.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerType;
import se.inera.ifv.v2.ErrorIdEnum;
import se.inera.ifv.v2.ResultCodeEnum;
import se.inera.ifv.v2.ResultOfCall;

@WebService(serviceName = "ReceiveMedicalCertificateAnswerResponderService", 
	    endpointInterface="se.inera.ifv.receivemedicalcertificateanswer.v1.rivtabp20.ReceiveMedicalCertificateAnswerResponderInterface", 
	    portName = "ReceiveMedicalCertificateAnswerResponderPort", 
	    targetNamespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateAnswer:1:rivtabp20",
	    wsdlLocation = "schemas/interactions/ReceiveMedicalCertificateAnswerInteraction/ReceiveMedicalCertificateAnswerInteraction_1.0_rivtabp20.wsdl")
public class ReceiveMedicalCertificateAnswerImpl implements ReceiveMedicalCertificateAnswerResponderInterface {
	
    private final static Logger log = LoggerFactory.getLogger(ReceiveMedicalCertificateAnswerImpl.class);

	private AnswerService answerService;
	
	@Resource
	public void setAnswerService(AnswerService answerService) {
		this.answerService = answerService;
	}
	
	public ReceiveMedicalCertificateAnswerResponseType receiveMedicalCertificateAnswer(
			org.w3.wsaddressing10.AttributedURIType logicalAddress,
			ReceiveMedicalCertificateAnswerType parameters) {

		ReceiveMedicalCertificateAnswerResponseType response = new ReceiveMedicalCertificateAnswerResponseType();

		try {			
			// The adress consist of an address in the format XX#caregiver#careunit or caregiver#careunit or only careunit
			String careGiverAndCareUnit = logicalAddress.getValue();
			String careUnit = "";
			if (careGiverAndCareUnit.indexOf("#") < 0) {
				careUnit = careGiverAndCareUnit;
			} else {
				careUnit = careGiverAndCareUnit.substring(careGiverAndCareUnit.lastIndexOf("#")+1, careGiverAndCareUnit.length());
			}

            log.debug("Received MedicalCertificateAnswer for care unit={}", careUnit);

			Answer answer = new Answer(careUnit, parameters.getAnswer());
			answerService.saveAnswer(answer);
			
			
			ResultOfCall value = new ResultOfCall();
			value.setResultCode(ResultCodeEnum.OK);
			response.setResult(value );
			
        } catch (Exception e) {
            log.warn("Error handling MedicalCertificateAnswer" ,e);
            // TODO: Fix this error handling
            response = new ReceiveMedicalCertificateAnswerResponseType();
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorIdEnum.APPLICATION_ERROR);
            response.getResult().setErrorText(e.getMessage());
        }

        return response;
	}
}
