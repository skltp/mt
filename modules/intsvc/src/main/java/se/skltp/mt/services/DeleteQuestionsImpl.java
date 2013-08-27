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

import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.core.service.QuestionService;
import se.skltp.mt.deletequestions.v1.rivtabp20.DeleteQuestionsResponderInterface;
import se.skltp.mt.deletequestionsresponder.v1.DeleteQuestionsResponseType;
import se.skltp.mt.deletequestionsresponder.v1.DeleteQuestionsType;
import se.skltp.mt.v2.ResultCodeEnum;
import se.skltp.mt.v2.ResultOfCall;

@WebService(serviceName = "DeleteQuestionsResponderService", 
            endpointInterface = "se.skltp.mt.deletequestions.v1.rivtabp20.DeleteQuestionsResponderInterface", 
            portName = "DeleteQuestionsResponderPort", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteQuestions:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/DeleteQuestionsInteraction/DeleteQuestionsInteraction_1.0_rivtabp20.wsdl")
public class DeleteQuestionsImpl implements DeleteQuestionsResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(DeleteQuestionsImpl.class);
    
    @Autowired
    QuestionService questionService;

    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    public DeleteQuestionsResponseType deleteQuestions(AttributedURIType address, DeleteQuestionsType parameters) {
        DeleteQuestionsResponseType response = new DeleteQuestionsResponseType();
        try {
            String careUnit = parameters.getCareUnitId().getExtension();
            log.debug("DeleteQuestions called for careunit:" + careUnit);

            List<String> idValues = parameters.getQuestionId();
            Set<Long> ids = newHashSet();
            for (String idValue : idValues) {
                ids.add(Long.parseLong(idValue));
            }

            questionService.deleteQuestionsForCareUnit(careUnit, ids);
            
            log.debug(ids.size() + " questions deleted for careunit:" + careUnit);
            
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.OK);

        } catch (Exception e) {
            log.warn("Error handling DeleteQuest message", e);
            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorText(e.getMessage());
        }
        return response;
    }
}
