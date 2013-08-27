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

package se.skltp.mt.inttest;

import static org.junit.Assert.*;

import org.junit.Test;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.mt.findallanswersresponder.v1.FindAllAnswersResponseType;
import se.skltp.mt.util.AnswersClient;
import se.skltp.mt.util.DbunitTestBase;


/**
 * @author Pär Wenåker
 *
 */
public class MedicalCertificateAnswerFlowTest extends DbunitTestBase {
    
    private AnswersClient client = new AnswersClient();
    
    @Test
    public void testSuccess() throws Exception {
        
        AttributedURIType logicalAddress = new AttributedURIType();
        logicalAddress.setValue("testCareUnit");

        for(int i = 0; i < 26 ; i++) {
            client.receive(logicalAddress);
        }
        
        assertEquals(26, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM ANSWER"));
        
        FindAllAnswersResponseType resp = client.findAllAnswers(logicalAddress);
        
        assertEquals(10, resp.getAnswers().getAnswer().size());
        assertEquals(16, resp.getAnswersLeft());

        client.deleteQuestions(logicalAddress, resp.getAnswers().getAnswer());

        assertEquals(16, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM ANSWER"));
       
        resp = client.findAllAnswers(logicalAddress);
        
        assertEquals(10, resp.getAnswers().getAnswer().size());
        assertEquals(6, resp.getAnswersLeft());

        client.deleteQuestions(logicalAddress, resp.getAnswers().getAnswer());

        assertEquals(6, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM ANSWER"));

        resp = client.findAllAnswers(logicalAddress);
        
        assertEquals(6, resp.getAnswers().getAnswer().size());
        assertEquals(0, resp.getAnswersLeft());

        client.deleteQuestions(logicalAddress, resp.getAnswers().getAnswer());
        
        assertEquals(0, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM ANSWER"));
    }

    
}
