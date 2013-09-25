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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.messagebox.core.ReceiverState;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.util.JpaRepositoryTestBase;
import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;

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
        Message message = new Message("hsaId", "orgId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));
    }

    @Test
    public void testFindByReceiver() throws Exception {
        String receiverId = "receivers Hsa-Id";
        Message message = new Message(receiverId, "orgId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        List<Message> messages = messageRepository.getMessages(receiverId);

        assertEquals(1, messages.size());
    }

    @Test
    public void testFindByReceiverAndId() throws Exception {
        Set<Long> ids = new HashSet<>();
        String receiverId = "receivers Hsa-Id";
        Message message = new Message(receiverId, "orgId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);
        ids.add(message.getId());

        entityManager.flush();
        entityManager.clear();

        List<Message> messages = messageRepository.getMessages(receiverId, ids);

        assertEquals(1, messages.size());
    }

    @Test
    public void testFindNone() throws Exception {
        Set<Long> ids = new HashSet<>();
        String receiverId = "receivers Hsa-Id";
        ids.add(1L);
        List<Message> messages = messageRepository.getMessages(receiverId, ids);
        assertEquals(0, messages.size());
    }

    @Test
    public void testOrder() throws Exception {
        Set<Long> ids = new HashSet<>();
        for(int i = 0 ; i < 5 ; i++) {
            Message message = new Message("hsaId", "org-1", "serviceContract", "webcallcontent");
            entityManager.persist(message);
            ids.add(message.getId());
        }
        
        entityManager.flush();
        entityManager.clear();
        
        List<Message> messages = messageRepository.getMessages("hsaId", ids);
        
        int first = messages.get(0).getId().intValue();
        assertEquals(first + 1, messages.get(1).getId().intValue());
        assertEquals(first + 2, messages.get(2).getId().intValue());
        assertEquals(first + 3, messages.get(3).getId().intValue());
        assertEquals(first + 4, messages.get(4).getId().intValue());
    }

    @Test
    public void testGetNumOfMessages() throws Exception {
        messageRepository.persist(new Message("hsaId", "orgId", "serviceContrakt", "webcall body"));
        messageRepository.persist(new Message("hsaId", "orgId", "serviceContrakt", "webcall body"));
        messageRepository.persist(new Message("hsaId", "orgId", "serviceContrakt", "webcall body"));

        entityManager.flush();
        entityManager.clear();
        
        long cnt = messageRepository.getNumOfMessagesForSystem("orgId");
        
        assertEquals(3, cnt);
    }

    @Test
    public void testDelete() throws Exception {
        String systemId = "hsaId";
        Message message = new Message(systemId, "orgId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));

        Set<Long> ids = new HashSet<>();
        ids.add(message.getId());

        // fail first delete because of wrong message status
        messageRepository.delete(systemId, ids);

        entityManager.flush();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));

        List<Message> messages = messageRepository.getMessages(systemId, ids);

        assertEquals(1, messages.size());
        messages.get(0).setStatusRetrieved();   // mark as retrived so we can delete it

        messageRepository.delete(systemId, ids);
        entityManager.flush();

        assertEquals(0, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));

    }

    @Test
    public void testCurrentState() throws Exception {
        Date time1 = new Date(System.currentTimeMillis() - 3600 * 1000);
        Date time2 = new Date(System.currentTimeMillis() - 3600 * 1000 * 2);
        Date time3 = new Date(System.currentTimeMillis() - 3600 * 1000 * 3);
        String receivingSystem = "hsaId";
        messageRepository.persist(new Message(receivingSystem, "orgId", "serviceContrakt", "webcall body", MessageStatusType.RECEIVED, time1));
        messageRepository.persist(new Message(receivingSystem, "orgId", "serviceContrakt", "webcall body", MessageStatusType.RECEIVED, time3));
        messageRepository.persist(new Message(receivingSystem, "orgId", "serviceContrakt", "webcall body", MessageStatusType.RECEIVED, time2));

        List<ReceiverState> receiverStates = messageRepository.getReceiverStatus();
        assertEquals(1, receiverStates.size());
        assertEquals(receivingSystem, receiverStates.get(0).getReceivingSystem());
        assertEquals(3, receiverStates.get(0).getNumberOfMessages());
        assertEquals(time3, receiverStates.get(0).getOldestMessage());
    }
}
