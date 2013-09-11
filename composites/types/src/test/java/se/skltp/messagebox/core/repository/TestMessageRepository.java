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
package se.skltp.messagebox.core.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.util.JpaRepositoryTestBase;
import se.skltp.messagebox.core.entity.Message;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestMessageRepository extends JpaRepositoryTestBase {

    @Autowired
    private MessageRepository messageRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testPersist() throws Exception {
        Message message = new Message("systemId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));
    }

    @Test
    public void testFindByCareUnit() throws Exception {
        Message message = new Message("systemId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        List<Message> messages = messageRepository.findAllForSystem("systemId");

        assertEquals(1, messages.size());
    }

    @Test
    public void testOrder() throws Exception {
        for(int i = 0 ; i < 5 ; i++) {
            entityManager.persist(new Message("system-1","serviceContract", "webcallcontent"));
        }
        
        entityManager.flush();
        entityManager.clear();
        
        List<Message> messages = messageRepository.findAllForSystem("system-1");
        
        int first = messages.get(0).getId().intValue();
        assertEquals(first + 1, messages.get(1).getId().intValue());
        assertEquals(first + 2, messages.get(2).getId().intValue());
        assertEquals(first + 3, messages.get(3).getId().intValue());
        assertEquals(first + 4, messages.get(4).getId().intValue());
    }

    @Test
    public void testGetNumOfMessages() throws Exception {
        messageRepository.persist(new Message("systemId", "serviceContrakt", "webcall body"));
        messageRepository.persist(new Message("systemId", "serviceContrakt", "webcall body"));
        messageRepository.persist(new Message("systemId", "serviceContrakt", "webcall body"));

        entityManager.flush();
        entityManager.clear();
        
        long cnt = messageRepository.getNumOfMessagesForSystem("systemId");
        
        assertEquals(3, cnt);
        
    }

    @Test
    public void testDelete() throws Exception {
        Message message = new Message("systemId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        messageRepository.remove(message.getId());

        entityManager.flush();

        assertEquals(0, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));
    }
}
