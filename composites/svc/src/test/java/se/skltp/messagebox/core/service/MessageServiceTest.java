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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:services-config.xml"})
public class MessageServiceTest extends JpaRepositoryTestBase {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TimeService timeService;

    @PersistenceContext
    EntityManager entityManager;

    private static final int MS_HOUR = 1000 * 60 * 60;

    @Test
    public void deleteFailWrongStatus() throws Exception {
        Message message = messageService.create("sourceId", "hsaId", "systemId", "serviceContrakt", "webcall body");

        entityManager.flush();

        try {
            messageService.deleteMessages("careUnit", timeService.now(), Collections.singletonList(message));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testDeleteCorrectStatus() throws Exception {
        String targetSys = "targetSys";
        Message message = messageService.create("sourceId", targetSys, "systemId", "serviceContrakt", "webcall body");
        messageService.getMessages(targetSys, Collections.singleton(message.getId()));

        entityManager.flush();

        List<Message> messages = messageService.listMessages(targetSys);

        messageService.deleteMessages(targetSys, timeService.now(), messages);
    }

    @Test
    public void testStatusReports() throws Exception {
        // the service layer just redirects to the repository layer, so there is nothing to test at this level
    }

}
