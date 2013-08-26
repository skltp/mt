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

package se.inera.ifv.util;

import java.util.List;

import org.w3.wsaddressing10.AttributedURIType;

import se.inera.ifv.deletequestions.v1.rivtabp20.DeleteQuestionsResponderInterface;
import se.inera.ifv.deletequestions.v1.rivtabp20.DeleteQuestionsResponderService;
import se.inera.ifv.deletequestionsresponder.v1.DeleteQuestionsType;
import se.inera.ifv.findallquestions.v1.rivtabp20.FindAllQuestionsResponderInterface;
import se.inera.ifv.findallquestions.v1.rivtabp20.FindAllQuestionsResponderService;
import se.inera.ifv.findallquestionsresponder.v1.FindAllQuestionsResponseType;
import se.inera.ifv.findallquestionsresponder.v1.FindAllQuestionsType;
import se.inera.ifv.findallquestionsresponder.v1.QuestionType;
import se.inera.ifv.qa.v1.Amnetyp;
import se.inera.ifv.qa.v1.InnehallType;
import se.inera.ifv.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderInterface;
import se.inera.ifv.receivemedicalcertificatequestion.v1.rivtabp20.ReceiveMedicalCertificateQuestionResponderService;
import se.inera.ifv.receivemedicalcertificatequestionsponder.v1.QuestionFromFkType;
import se.inera.ifv.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionResponseType;
import se.inera.ifv.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionType;

/**
 * @author Pär Wenåker
 *
 */
public class QuestionsClient extends ClientBase {
    /**
     * @param question
     */
    public void deleteQuestions(AttributedURIType logicalAddress, List<QuestionType> questions) {

        String SERVICE_ADDRESS = getServiceBaseUrl() + "DeleteQuestions/1/rivtabp20";

        DeleteQuestionsResponderInterface service = new DeleteQuestionsResponderService(
                createEndpointUrlFromServiceAddress(SERVICE_ADDRESS))
                .getDeleteQuestionsResponderPort();
        
        DeleteQuestionsType parameters = new DeleteQuestionsType();
        for(QuestionType q : questions) {
            parameters.getQuestionId().add(q.getId());
        }
        
        service.deleteQuestions(logicalAddress, parameters);
        
    }

    public ReceiveMedicalCertificateQuestionResponseType receive(AttributedURIType logicalAddress) {

        String SERVICE_ADDRESS = getServiceBaseUrl() + "RecMedCertQuestion/1/rivtabp20";
        
        ReceiveMedicalCertificateQuestionResponderInterface service = new ReceiveMedicalCertificateQuestionResponderService(
                createEndpointUrlFromServiceAddress(SERVICE_ADDRESS))
                .getReceiveMedicalCertificateQuestionResponderPort();

        ReceiveMedicalCertificateQuestionType request = new ReceiveMedicalCertificateQuestionType();

        // Simple Question
        QuestionFromFkType meddelande = new QuestionFromFkType();
        request.setQuestion(meddelande);
        meddelande.setAmne(Amnetyp.KONTAKT);
        InnehallType fraga = new InnehallType();
        fraga.setMeddelandeText("Kontakta mig!");
        meddelande.setFraga(fraga);

        ReceiveMedicalCertificateQuestionResponseType result = null;
        try {
            result = service.receiveMedicalCertificateQuestion(logicalAddress, request);
        } catch (Exception ex) {
            System.out.println("Exception=" + ex.getMessage());
        }
        return result;
    }
    
    public FindAllQuestionsResponseType findAllQuestions(AttributedURIType logicalAddress) {
        
        String SERVICE_ADDRESS = getServiceBaseUrl() + "FindAllQuestions/1/rivtabp20";
        
        FindAllQuestionsResponderInterface service = new FindAllQuestionsResponderService(                
                createEndpointUrlFromServiceAddress(SERVICE_ADDRESS))
                .getFindAllQuestionsResponderPort();
        
        FindAllQuestionsType parameters = new FindAllQuestionsType();
        
        return service.findAllQuestions(logicalAddress, parameters);
    }    

}
