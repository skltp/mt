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
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.MessageStatus;
import se.skltp.messagebox.core.service.TimeService;
import se.skltp.messagebox.util.JpaRepositoryTestBase;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class TestMessageRepository extends JpaRepositoryTestBase {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TimeService timeService;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testPersist() throws Exception {
        messageRepository.create("sourceId", "hsaId", "orgId", "serviceContrakt", "webcall body");

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));
    }

    @Test
    public void testFindByReceiver() throws Exception {
        String targetSystem = "receivers Hsa-Id";
        messageRepository.create("sourceId", targetSystem, "orgId", "serviceContrakt", "webcall body");

        entityManager.flush();
        entityManager.clear();

        List<Message> messages = messageRepository.listMessages(targetSystem);

        assertEquals(1, messages.size());
    }

    @Test
    public void testFindByReceiverAndId() throws Exception {
        Set<Long> ids = new HashSet<Long>();
        String targetSystem = "receivers Hsa-Id";
        Message message = messageRepository.create("sourceId", targetSystem, "orgId", "serviceContrakt", "webcall body");
        ids.add(message.getId());

        entityManager.flush();
        entityManager.clear();

        List<Message> messages = messageRepository.getMessages(targetSystem, ids);

        assertEquals(1, messages.size());
    }

    @Test
    public void testFindNone() throws Exception {
        Set<Long> ids = new HashSet<Long>();
        String targetSystem = "receivers Hsa-Id";
        ids.add(1L);
        List<Message> messages = messageRepository.getMessages(targetSystem, ids);
        assertEquals(0, messages.size());
    }

    @Test
    public void testOrder() throws Exception {
        Set<Long> ids = new HashSet<Long>();
        for(int i = 0 ; i < 5 ; i++) {
            Message message = messageRepository.create("sourceId", "hsaId", "org-1", "serviceContract", "webcallcontent");
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
    public void testDelete() throws Exception {
        String systemId = "hsaId";
        Message message = messageRepository.create("sourceId", systemId, "orgId", "serviceContrakt", "webcall body");
        messageRepository.persist(message);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, simpleJdbcTemplate.queryForInt("SELECT COUNT(*) FROM MESSAGE"));

        Set<Long> ids = new HashSet<Long>();
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
        Date time1 = new Date(timeService.now() - 3600 * 1000);
        Date time2 = new Date(timeService.now() - 3600 * 1000 * 2);
        Date time3 = new Date(timeService.now() - 3600 * 1000 * 3);
        String rec1 = "hsaId1";
        String rec2 = "hsaId2";
        String org1 = "org1";
        String org2 = "org2";
        String org3 = "org3";
        String sc1 = "serviceContract1";
        String sc2 = "serviceContract2";

        messageRepository.persist(new Message("sourceId", rec2, org2, sc1, "webcall body", MessageStatus.RECEIVED, time2));
        messageRepository.persist(new Message("sourceId", rec1, org1, sc1, "webcall body", MessageStatus.RECEIVED, time3));
        messageRepository.persist(new Message("sourceId", rec1, org1, sc2, "webcall body", MessageStatus.RECEIVED, time2));
        messageRepository.persist(new Message("sourceId", rec2, org3, sc1, "webcall body", MessageStatus.RECEIVED, time2));
        messageRepository.persist(new Message("sourceId", rec1, org1, sc1, "webcall body", MessageStatus.RECEIVED, time1));
        messageRepository.persist(new Message("sourceId", rec2, org3, sc2, "webcall body", MessageStatus.RECEIVED, time1));
        messageRepository.persist(new Message("sourceId", rec2, org3, sc2, "webcall body", MessageStatus.RECEIVED, time3));

        List<StatusReport> reports = messageRepository.getStatusReports();
        assertEquals(5, reports.size());
        // order is important, so we can compare directly

        // 1/1/1, 2 msg, time3
        StatusReport sr = reports.get(0);
        assertEquals(rec1, sr.getTargetSystem());
        assertEquals(org1, sr.getTargetOrganization());
        assertEquals(sc1, sr.getServiceContract());
        assertEquals(2, sr.getMessageCount());
        assertEquals(time3, sr.getOldestMessageDate());

        // 1/1/2, 1 msg, time2
        sr = reports.get(1);
        assertEquals(rec1, sr.getTargetSystem());
        assertEquals(org1, sr.getTargetOrganization());
        assertEquals(sc2, sr.getServiceContract());
        assertEquals(1, sr.getMessageCount());
        assertEquals(time2, sr.getOldestMessageDate());

        // 2/2/1, 1 msg, time2
        sr = reports.get(2);
        assertEquals(rec2, sr.getTargetSystem());
        assertEquals(org2, sr.getTargetOrganization());
        assertEquals(sc1, sr.getServiceContract());
        assertEquals(1, sr.getMessageCount());
        assertEquals(time2, sr.getOldestMessageDate());

        // 2/3/1, 1 msg, time2
        sr = reports.get(3);
        assertEquals(rec2, sr.getTargetSystem());
        assertEquals(org3, sr.getTargetOrganization());
        assertEquals(sc1, sr.getServiceContract());
        assertEquals(1, sr.getMessageCount());
        assertEquals(time2, sr.getOldestMessageDate());

        // 2/3/2, 2 msg, time3
        sr = reports.get(4);
        assertEquals(rec2, sr.getTargetSystem());
        assertEquals(org3, sr.getTargetOrganization());
        assertEquals(sc2, sr.getServiceContract());
        assertEquals(2, sr.getMessageCount());
        assertEquals(time3, sr.getOldestMessageDate());

    }
}
