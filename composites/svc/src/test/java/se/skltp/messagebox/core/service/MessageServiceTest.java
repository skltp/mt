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
package se.skltp.messagebox.core.service;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.exception.ServiceContractTypeNotStorableException;
import se.skltp.messagebox.util.JpaRepositoryTestBase;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class MessageServiceTest extends JpaRepositoryTestBase {

    @Autowired
    private MessageService messageService;

    @PersistenceContext
    EntityManager entityManager;

    MessageStatusType type;

    @Test
    public void deleteMessages() throws Exception {
        Message message = new Message("systemId", "serviceContrakt", "webcall body");
        messageService.saveMessage(message);

        entityManager.flush();

        Set<Long> ids = new HashSet<Long>();
        ids.add(message.getId());
        try {
            messageService.deleteMessagesForSystem("careUnit", ids);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // TODO: How can we check that the transaction is marked for rolled back?
    }

    @Test
    public void testResponses() throws Exception {

        String okServiceContract = "urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3";
        String notOkServiceContract = "not ok";

        String response = messageService.getOkResponseForServiceContract(okServiceContract);
        assertEquals("<result>OK</result>", response);

        try {
            messageService.getOkResponseForServiceContract(notOkServiceContract);
            fail("Should have thrown");
        } catch (ServiceContractTypeNotStorableException e) {
            // ok
        }

    }

}
