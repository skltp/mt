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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.core.entity.Question;
import se.skltp.mt.core.service.QuestionService;
import se.skltp.mt.core.service.QuestionsValue;
import se.skltp.mt.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface;
import se.skltp.mt.findallquestionsresponder.v1.FindAllQuestionsResponseType;
import se.skltp.mt.findallquestionsresponder.v1.FindAllQuestionsType;
import se.skltp.mt.findallquestionsresponder.v1.QuestionType;
import se.skltp.mt.findallquestionsresponder.v1.QuestionsType;
import se.skltp.mt.receivemedicalcertificatequestionsponder.v1.QuestionFromFkType;
import se.skltp.mt.v2.ResultCodeEnum;
import se.skltp.mt.v2.ResultOfCall;

@WebService(serviceName = "FindAllQuestionsResponderService", 
            endpointInterface = "se.skltp.mt.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface", 
            portName = "FindAllQuestionsResponderPort", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:FindAllQuestions:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/FindAllQuestionsInteraction/FindAllQuestionsInteraction_1.0_rivtabp20.wsdl")
public class FindAllQuestionsImpl implements FindAllQuestionsResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(FindAllQuestionsImpl.class);
    
    private QuestionService questionService;

    @Resource
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public FindAllQuestionsResponseType findAllQuestions(AttributedURIType address, FindAllQuestionsType parameters) {
        FindAllQuestionsResponseType response = new FindAllQuestionsResponseType();
        try {
            String careUnit = parameters.getCareUnitId().getExtension();
            log.debug("FindAllQuestions called for careunit:" + careUnit);
            
            QuestionsValue questionValue = questionService.getQuestionsForCareUnit(careUnit);

            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.OK);
            response.setQuestionsLeft(questionValue.getQuestionsLeft());
            response.setQuestions(new QuestionsType());

            for (Question q : questionValue.getQuestions()) {
                QuestionFromFkType questionFromFk = (QuestionFromFkType) q.getMessage();
                QuestionType qt = new QuestionType();
                qt.setId(q.getId().toString());
                qt.setReceivedDate(q.getArrived());
                qt.setQuestion(questionFromFk);

                response.getQuestions().getQuestion().add(qt);

                q.setStatusRetrieved();
            }

            log.debug("FindAllQuestions found " + questionValue.getQuestions().size() +  " questions for careunit " + careUnit);

        } catch (Exception e) {
            log.warn("Failed to handle FindAllQuestions", e);
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorText(e.getMessage());            
        }
        return response;
    }
}
