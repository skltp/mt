/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera MessageService (http://code.google.com/p/inera-message).
 *
 * Inera MessageService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera MessageService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        Message message = messageService.create("sourceId", "hsaId", "systemId", "serviceContrakt", "webcall body");

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
        Message message = new Message("sourceId", "hsaId", "systemId", "serviceContrakt", "webcall body", MessageStatus.RECEIVED, oldArrivalDate);
        messageService.saveMessage(message);

        entityManager.flush();
        // TODO: Incomplete, fix
    }

}
