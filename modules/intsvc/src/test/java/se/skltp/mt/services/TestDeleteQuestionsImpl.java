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

import java.io.InputStream;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.easymock.Capture;
import org.junit.Test;
import org.w3.wsaddressing10.AttributedURIType;
import se.skltp.mt.core.service.QuestionService;
import se.skltp.mt.deletequestionsresponder.v1.DeleteQuestionsType;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author P�r Wen�ker
 *
 */
public class TestDeleteQuestionsImpl {

    @Test
    public void testDeleteQuestions() throws Exception {
        Capture<String> careUnit = new Capture<String>();
        Capture<Set<Long>> ids = new Capture<Set<Long>>();

        QuestionService questionService = createMock(QuestionService.class);
        questionService.deleteQuestionsForCareUnit(capture(careUnit), capture(ids));
        expectLastCall();

        DeleteQuestionsImpl impl = new DeleteQuestionsImpl();
        impl.setQuestionService(questionService);

        replay(questionService);

        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("DeleteQuestionsResponder_0.9.xml");

        JAXBContext jaxbContext = JAXBContext.newInstance(DeleteQuestionsType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        @SuppressWarnings("unchecked")
        JAXBElement<DeleteQuestionsType> t = (JAXBElement<DeleteQuestionsType>) unmarshaller.unmarshal(is);
        AttributedURIType address = new AttributedURIType();
        address.setValue("careUnit1");
        
        impl.deleteQuestions(address, t.getValue());

        assertNotNull(careUnit.getValue());
        assertNotNull(ids.getValue());
        
        assertEquals("careUnit1", careUnit.getValue());
        assertEquals(8, ids.getValue().size());
        assertTrue(ids.getValue().contains(1L));
        assertTrue(ids.getValue().contains(5L));
        assertTrue(ids.getValue().contains(6L));
        assertTrue(ids.getValue().contains(12L));
        assertTrue(ids.getValue().contains(19L));
        assertTrue(ids.getValue().contains(23L));
        assertTrue(ids.getValue().contains(26L));
        assertTrue(ids.getValue().contains(29L));
    }

}
