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

package se.skltp.mt.util;

import java.util.List;

import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.deleteanswers.v1.rivtabp20.DeleteAnswersResponderInterface;
import se.skltp.mt.deleteanswers.v1.rivtabp20.DeleteAnswersResponderService;
import se.skltp.mt.deleteanswersresponder.v1.DeleteAnswersType;
import se.skltp.mt.findallanswers.v1.rivtabp20.FindAllAnswersResponderInterface;
import se.skltp.mt.findallanswers.v1.rivtabp20.FindAllAnswersResponderService;
import se.skltp.mt.findallanswersresponder.v1.AnswerType;
import se.skltp.mt.findallanswersresponder.v1.FindAllAnswersResponseType;
import se.skltp.mt.findallanswersresponder.v1.FindAllAnswersType;
import se.skltp.mt.qa.v1.Amnetyp;
import se.skltp.mt.qa.v1.InnehallType;
import se.skltp.mt.receivemedicalcertificateanswer.v1.rivtabp20.ReceiveMedicalCertificateAnswerResponderInterface;
import se.skltp.mt.receivemedicalcertificateanswer.v1.rivtabp20.ReceiveMedicalCertificateAnswerResponderService;
import se.skltp.mt.receivemedicalcertificateanswerresponder.v1.AnswerFromFkType;
import se.skltp.mt.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerResponseType;
import se.skltp.mt.receivemedicalcertificateanswerresponder.v1.ReceiveMedicalCertificateAnswerType;

/**
 * @author Pär Wenåker
 *
 */
public class AnswersClient extends ClientBase {
    
    /**
     * @param question
     */
    public void deleteQuestions(AttributedURIType logicalAddress, List<AnswerType> answer) {

        String SERVICE_ADDRESS = getServiceBaseUrl() + "DeleteAnswers/1/rivtabp20";

        DeleteAnswersResponderInterface service = new DeleteAnswersResponderService(
                createEndpointUrlFromServiceAddress(SERVICE_ADDRESS))
                .getDeleteAnswersResponderPort();
        
        DeleteAnswersType parameters = new DeleteAnswersType();
        for(AnswerType a : answer) {
            parameters.getAnswerId().add(a.getId());
        }
        
        service.deleteAnswers(logicalAddress, parameters);
        
    }

    public ReceiveMedicalCertificateAnswerResponseType receive(AttributedURIType logicalAddress) {

        String SERVICE_ADDRESS = getServiceBaseUrl() + "RecMedCertAnswer/1/rivtabp20";
        
        ReceiveMedicalCertificateAnswerResponderInterface service = new ReceiveMedicalCertificateAnswerResponderService(
                createEndpointUrlFromServiceAddress(SERVICE_ADDRESS))
                .getReceiveMedicalCertificateAnswerResponderPort();

        ReceiveMedicalCertificateAnswerType request = new ReceiveMedicalCertificateAnswerType();

        // Simple Question
        AnswerFromFkType meddelande = new AnswerFromFkType();
        request.setAnswer(meddelande);
        meddelande.setAmne(Amnetyp.KONTAKT);
        InnehallType fraga = new InnehallType();
        fraga.setMeddelandeText("Kontakta mig!");
        meddelande.setFraga(fraga);

        ReceiveMedicalCertificateAnswerResponseType result = null;
        try {
            result = service.receiveMedicalCertificateAnswer(logicalAddress, request);
        } catch (Exception ex) {
            System.out.println("Exception=" + ex.getMessage());
        }
        return result;
    }
    
    public FindAllAnswersResponseType findAllAnswers(AttributedURIType logicalAddress) {
        
        String SERVICE_ADDRESS = getServiceBaseUrl() + "FindAllAnswers/1/rivtabp20";
        
        FindAllAnswersResponderInterface service = new FindAllAnswersResponderService(                
                createEndpointUrlFromServiceAddress(SERVICE_ADDRESS))
                .getFindAllAnswersResponderPort();
        
        FindAllAnswersType parameters = new FindAllAnswersType();
        
        return service.findAllAnswers(logicalAddress, parameters);
    }

}
