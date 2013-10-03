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

import java.util.Collections;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.MessageStatus;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestMessageService extends JpaRepositoryTestBase {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TimeService timeService;

    @PersistenceContext
    EntityManager entityManager;

    private static final int MS_HOUR = 1000 * 60 * 60;

    @Test
    public void deleteMessages() throws Exception {
        Message message = messageService.create("sourceId", "hsaId", "systemId", "serviceContrakt", "webcall body", "correlationId");

        entityManager.flush();

        try {
            messageService.deleteMessages("careUnit", timeService.now(), Collections.singletonList(message));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // TODO: How can we check that the transaction is marked for rolled back?
    }


    @Test
    public void testStatistics() throws Exception {

        Date oldArrivalDate = new Date(timeService.now() - 48 * MS_HOUR);
        Message message = new Message("sourceId", "hsaId", "systemId", "serviceContrakt", "webcall body", MessageStatus.RECEIVED, oldArrivalDate, "correlationId");
        messageService.saveMessage(message);

        entityManager.flush();
        // TODO: Incomplete, fix
    }

}
