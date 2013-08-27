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

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.easymock.Capture;
import org.junit.Test;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.core.entity.Question;
import se.skltp.mt.core.service.QuestionService;
import se.skltp.mt.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionResponseType;
import se.skltp.mt.receivemedicalcertificatequestionsponder.v1.ReceiveMedicalCertificateQuestionType;
import se.skltp.mt.services.ReceiveMedicalCertificateQuestionImpl;
import se.skltp.mt.v2.ErrorIdEnum;
import se.skltp.mt.v2.ResultCodeEnum;

public class TestReceiveMedicalCertificateQuestionImpl {

    @Test
    public void testInvoke() throws Exception {
        Capture<Question> question = new Capture<Question>();

        QuestionService questionService = createMock(QuestionService.class);
        expect(questionService.saveQuestion(capture(question))).andReturn(1L);

        ReceiveMedicalCertificateQuestionImpl impl = new ReceiveMedicalCertificateQuestionImpl();
        impl.setQuestionService(questionService);

        replay(questionService);

        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("ReceiveMedicalCertificateQuestionResponder_0.9.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(ReceiveMedicalCertificateQuestionType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        @SuppressWarnings("unchecked")
        JAXBElement<ReceiveMedicalCertificateQuestionType> t = (JAXBElement<ReceiveMedicalCertificateQuestionType>) unmarshaller
                .unmarshal(is);
        AttributedURIType logicalAddress = new AttributedURIType();
        logicalAddress.setValue("careUnit1");

        ReceiveMedicalCertificateQuestionResponseType response = impl.receiveMedicalCertificateQuestion(logicalAddress,
                t.getValue());

        assertEquals("careUnit1", question.getValue().getCareUnit());
        assertNotNull(question.getValue().getArrived());
        assertNull(question.getValue().getId());
        assertEquals(ResultCodeEnum.OK, response.getResult().getResultCode());
    }
    
    @Test
    public void testInvokeError() throws Exception {
        Capture<Question> question = new Capture<Question>();

        QuestionService questionService = createMock(QuestionService.class);
        expect(questionService.saveQuestion(capture(question))).andThrow(new RuntimeException("Some message"));

        ReceiveMedicalCertificateQuestionImpl impl = new ReceiveMedicalCertificateQuestionImpl();
        impl.setQuestionService(questionService);

        replay(questionService);

        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("ReceiveMedicalCertificateQuestionResponder_0.9.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(ReceiveMedicalCertificateQuestionType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        @SuppressWarnings("unchecked")
        JAXBElement<ReceiveMedicalCertificateQuestionType> t = (JAXBElement<ReceiveMedicalCertificateQuestionType>) unmarshaller
                .unmarshal(is);
        AttributedURIType logicalAddress = new AttributedURIType();
        logicalAddress.setValue("careUnit1");

        ReceiveMedicalCertificateQuestionResponseType response = impl.receiveMedicalCertificateQuestion(logicalAddress,
                t.getValue());

        assertEquals(ResultCodeEnum.ERROR, response.getResult().getResultCode());
        assertEquals(ErrorIdEnum.APPLICATION_ERROR, response.getResult().getErrorId());
        assertEquals("Some message", response.getResult().getErrorText());

    }
}
