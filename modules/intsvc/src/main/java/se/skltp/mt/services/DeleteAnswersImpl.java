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

import se.skltp.mt.core.service.AnswerService;
import se.skltp.mt.deleteanswers.v1.rivtabp20.DeleteAnswersResponderInterface;
import se.skltp.mt.deleteanswersresponder.v1.DeleteAnswersResponseType;
import se.skltp.mt.deleteanswersresponder.v1.DeleteAnswersType;
import se.skltp.mt.v2.ResultCodeEnum;
import se.skltp.mt.v2.ResultOfCall;

@WebService(serviceName = "DeleteAnswersResponderService", 
            endpointInterface = "se.skltp.mt.deleteanswers.v1.rivtabp20.DeleteAnswersResponderInterface", 
            portName = "DeleteAnswersResponderPort", 
            targetNamespace = "urn:riv:insuranceprocess:healthreporting:DeleteAnswers:1:rivtabp20", 
            wsdlLocation = "schemas/interactions/DeleteAnswersInteraction/DeleteAnswersInteraction_1.0_rivtabp20.wsdl")
public class DeleteAnswersImpl implements DeleteAnswersResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(DeleteAnswersImpl.class);
    
    @Autowired
    AnswerService answerService;

    /**
     * @param answerService the answerService to set
     */
    public void setAnswerService(AnswerService answerService) {
        this.answerService = answerService;
    }
    
    public DeleteAnswersResponseType deleteAnswers(AttributedURIType address, DeleteAnswersType parameters) {
        DeleteAnswersResponseType response = new DeleteAnswersResponseType();
        try {
            String careUnit = parameters.getCareUnitId().getExtension();
            log.debug("DeleteAnswers called for careunit:" + careUnit);

            List<String> idValues = parameters.getAnswerId();
            Set<Long> ids = newHashSet();
            for (String idValue : idValues) {
                ids.add(Long.parseLong(idValue));
            }
            answerService.deleteAnswersForCareUnit(careUnit, ids);

            log.debug(ids.size() + " answers deleted for careunit:" + careUnit);

            response.setResult(new ResultOfCall());
            response.getResult().setResultCode(ResultCodeEnum.OK);

        } catch (Exception e) {
            log.warn("Error handling DeleteAnswer message", e);
            // FIXME: Improved error handling
            response.setResult(new ResultOfCall());
            response.getResult().setErrorText(e.getMessage());
            response.getResult().setResultCode(ResultCodeEnum.ERROR);
        }
        return response;
    }
}
