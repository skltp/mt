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

import se.inera.ifv.casebox.core.entity.Answer;
import se.inera.ifv.casebox.core.service.AnswerService;
import se.inera.ifv.casebox.core.service.AnswersValue;
import se.inera.ifv.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface;
import se.inera.ifv.findallanswersresponder.v1.AnswerType;
import se.inera.ifv.findallanswersresponder.v1.AnswersType;
import se.inera.ifv.findallanswersresponder.v1.FindAllAnswersResponseType;
import se.inera.ifv.findallanswersresponder.v1.FindAllAnswersType;
import se.inera.ifv.receivemedicalcertificateanswerresponder.v1.AnswerFromFkType;
import se.inera.ifv.v2.ResultCodeEnum;
import se.inera.ifv.v2.ResultOfCall;

@WebService(serviceName = "FindAllAnswersResponderService", 
            endpointInterface = "se.inera.ifv.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface", 
            portName = "FindAllAnswersResponderPort", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:FindAllAnswers:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/FindAllAnswersInteraction/FindAllAnswersInteraction_1.0_rivtabp20.wsdl")
public class FindAllAnswersImpl implements FindAllAnswersResponderInterface {

	private static final Logger log = LoggerFactory.getLogger(FindAllAnswersImpl.class);

    private AnswerService answerService;

    @Resource
    public void setAnswerService(AnswerService questionService) {
        this.answerService = questionService;
    }

    public FindAllAnswersResponseType findAllAnswers(AttributedURIType address, FindAllAnswersType parameters) {
        FindAllAnswersResponseType response = new FindAllAnswersResponseType();
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

        } catch (Exception e) {
            // FIXME: Add better error handling
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorText(e.getMessage());
        }
        return response;
    }
}
