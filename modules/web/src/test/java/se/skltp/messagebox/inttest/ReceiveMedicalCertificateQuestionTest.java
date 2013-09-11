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

package se.skltp.messagebox.inttest;

import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Test;
import org.w3.wsaddressing10.AttributedURIType;

import se.skltp.messagebox.util.DbunitTestBase;
import se.skltp.messagebox.util.QuestionsClient;


/**
 * @author Pär Wenåker
 *
 */
public class ReceiveMedicalCertificateQuestionTest extends DbunitTestBase {

    QuestionsClient client = new QuestionsClient();
    
    @Test
    public void test() throws Exception {
       
        AttributedURIType logicalAddress = new AttributedURIType();
        logicalAddress.setValue("careUnit1");

        client.receive(logicalAddress);

        ITable result = getConnection().createQueryTable("ANSWER", "SELECT * FROM QUESTION WHERE CARE_UNIT = 'careUnit1'");
        Assert.assertEquals(1, result.getRowCount());
        Assert.assertNotNull(result.getValue(0, "MESSAGE"));
    }

}
