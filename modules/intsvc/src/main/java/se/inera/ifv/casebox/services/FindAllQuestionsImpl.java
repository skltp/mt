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
import se.inera.ifv.casebox.core.service.QuestionsValue;
import se.inera.ifv.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface;
import se.inera.ifv.findallquestionsresponder.v1.FindAllQuestionsResponseType;
import se.inera.ifv.findallquestionsresponder.v1.FindAllQuestionsType;
import se.inera.ifv.findallquestionsresponder.v1.QuestionType;
import se.inera.ifv.findallquestionsresponder.v1.QuestionsType;
import se.inera.ifv.receivemedicalcertificatequestionsponder.v1.QuestionFromFkType;
import se.inera.ifv.v2.ResultCodeEnum;
import se.inera.ifv.v2.ResultOfCall;

@WebService(serviceName = "FindAllQuestionsResponderService", 
            endpointInterface = "se.inera.ifv.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface", 
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
