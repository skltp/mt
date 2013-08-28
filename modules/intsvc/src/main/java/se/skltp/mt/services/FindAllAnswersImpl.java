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
package se.skltp.mt.services;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.cxf.interceptor.InInterceptors;
import org.apache.cxf.interceptor.OutInterceptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.core.entity.Answer;
import se.skltp.mt.core.service.AnswerService;
import se.skltp.mt.core.service.AnswersValue;
import se.skltp.mt.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface;
import se.skltp.mt.findallanswersresponder.v1.AnswerType;
import se.skltp.mt.findallanswersresponder.v1.AnswersType;
import se.skltp.mt.findallanswersresponder.v1.FindAllAnswersResponseType;
import se.skltp.mt.findallanswersresponder.v1.FindAllAnswersType;
import se.skltp.mt.receivemedicalcertificateanswerresponder.v1.AnswerFromFkType;
import se.skltp.mt.v2.ResultCodeEnum;
import se.skltp.mt.v2.ResultOfCall;

@WebService(serviceName = "FindAllAnswersResponderService", 
            endpointInterface = "se.skltp.mt.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface", 
            portName = "FindAllAnswersResponderPort", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:FindAllAnswers:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/FindAllAnswersInteraction/FindAllAnswersInteraction_1.0_rivtabp20.wsdl")
@InInterceptors(interceptors = "org.apache.cxf.interceptor.LoggingInInterceptor")
@OutInterceptors(interceptors = "org.apache.cxf.interceptor.LoggingOutInterceptor")
public class FindAllAnswersImpl implements FindAllAnswersResponderInterface {

	private static final Logger log = LoggerFactory.getLogger(FindAllAnswersImpl.class);

    private AnswerService answerService;

    @Resource
    public void setAnswerService(AnswerService questionService) {
        this.answerService = questionService;
    }

    public FindAllAnswersResponseType findAllAnswers(AttributedURIType address, FindAllAnswersType parameters) {
        FindAllAnswersResponseType response = new FindAllAnswersResponseType();
        System.err.println("Calling findAllAnswers, careUnit " + parameters.getCareUnitId());
        try {
            String careUnit = parameters.getCareUnitId().getExtension();
            log.debug("FindAllAnswers called for careunit:" + careUnit);

            AnswersValue answerValue = answerService.getAnswersForCareUnit(careUnit);

            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.OK);
            response.setAnswersLeft(answerValue.getAnswersLeft());
            response.setAnswers(new AnswersType());

            for (Answer a : answerValue.getAnswers()) {
                AnswerFromFkType answerFromFk = (AnswerFromFkType) a.getMessage();
                AnswerType qt = new AnswerType();
                qt.setId(a.getId().toString());
                qt.setReceivedDate(a.getArrived());
                qt.setAnswer(answerFromFk);

                response.getAnswers().getAnswer().add(qt);

                a.setStatusRetrieved();
            }

            log.debug("FindAllAnswers found " + answerValue.getAnswers().size() +  " answers for careunit " + careUnit);
            System.err.println("Exiting findAllAnswers");
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("Error in FindAllAnswers!", e);
            // FIXME: Add better error handling
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorText(e.getMessage());
        }
        return response;
    }
}
