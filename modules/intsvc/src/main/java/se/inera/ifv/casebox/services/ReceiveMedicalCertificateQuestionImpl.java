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
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.casebox.core.entity.Question;
import se.inera.ifv.casebox.core.service.QuestionService;
import se.inera.ifv.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionResponseType;
import se.inera.ifv.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionType;
import se.inera.ifv.v2.ErrorIdEnum;
import se.inera.ifv.v2.ResultCodeEnum;
import se.inera.ifv.v2.ResultOfCall;

@WebService(portName = "ReceiveMedicalCertificateQuestionResponderPort", 
            endpointInterface = "se.inera.ifv.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderInterface", 
            serviceName = "ReceiveMedicalCertificateQuestionResponderService", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:ReceiveMedicalCertificateQuestion:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/ReceiveMedicalCertificateQuestionInteraction/ReceiveMedicalCertificateQuestionInteraction_1.0_rivtabp20.wsdl")
public class ReceiveMedicalCertificateQuestionImpl implements ReceiveMedicalCertificateQuestionResponderInterface {

    private final static Logger log = LoggerFactory.getLogger(ReceiveMedicalCertificateQuestionImpl.class);

    private QuestionService questionService;

    @Resource
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public ReceiveMedicalCertificateQuestionResponseType receiveMedicalCertificateQuestion(
            AttributedURIType logicalAddress, ReceiveMedicalCertificateQuestionType parameters) {

        // TODO: Should we have a default error response here?
        ReceiveMedicalCertificateQuestionResponseType response = null;

        try {
			// The adress consist of an address in the format XX#caregiver#careunit or caregiver#careunit or only careunit
			String careGiverAndCareUnit = logicalAddress.getValue();
			String careUnit = "";
			if (careGiverAndCareUnit.indexOf("#") < 0) {
				careUnit = careGiverAndCareUnit;
			} else {
				careUnit = careGiverAndCareUnit.substring(careGiverAndCareUnit.lastIndexOf("#")+1, careGiverAndCareUnit.length());
			}

            log.debug("Received MedicalCertificateQuestion for care unit={}", careUnit);

            Question question = new Question(careUnit, parameters.getQuestion());

            questionService.saveQuestion(question);

            response = new ReceiveMedicalCertificateQuestionResponseType();
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.OK);
            
        } catch (Exception e) {
            log.warn("Error handling MedicalCertificateQuestion" ,e);
            // TODO: Fix this error handling
            response = new ReceiveMedicalCertificateQuestionResponseType();
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorIdEnum.APPLICATION_ERROR);
            response.getResult().setErrorText(e.getMessage());
        }

        return response;
    }
}
