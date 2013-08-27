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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.easymock.Capture;
import org.junit.Test;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.core.service.AnswerService;
import se.skltp.mt.deleteanswersresponder.v1.DeleteAnswersType;
import se.skltp.mt.services.DeleteAnswersImpl;

/**
 * @author Pär Wenåker
 *
 */
public class TestDeleteAnswersImpl {

    @Test
    public void testDeleteAnswers() throws Exception {
        Capture<String> careUnit = new Capture<String>();
        Capture<Set<Long>> ids = new Capture<Set<Long>>();

        AnswerService answerService = createMock(AnswerService.class);
        answerService.deleteAnswersForCareUnit(capture(careUnit), capture(ids));
        expectLastCall();

        DeleteAnswersImpl impl = new DeleteAnswersImpl();
        impl.setAnswerService(answerService);
        
        replay(answerService);

        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("DeleteAnswersResponder_0.9.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(DeleteAnswersType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        @SuppressWarnings("unchecked")
        JAXBElement<DeleteAnswersType> t = (JAXBElement<DeleteAnswersType>) unmarshaller.unmarshal(is);
        AttributedURIType address = new AttributedURIType();
        address.setValue("careUnit1");
        
        impl.deleteAnswers(address, t.getValue());

        assertNotNull(careUnit.getValue());
        assertNotNull(ids.getValue());
        
        assertEquals("careUnit1", careUnit.getValue());
        assertEquals(5, ids.getValue().size());
        assertTrue(ids.getValue().contains(1L));
        assertTrue(ids.getValue().contains(12L));
        assertTrue(ids.getValue().contains(21L));
        assertTrue(ids.getValue().contains(33L));
        assertTrue(ids.getValue().contains(88L));
    }

}
