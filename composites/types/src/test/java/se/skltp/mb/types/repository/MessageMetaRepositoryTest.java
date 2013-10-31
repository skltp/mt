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
package se.skltp.mb.types.repository;

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
import se.skltp.mb.types.StatusReport;
import se.skltp.mb.types.entity.MessageMeta;
import se.skltp.mb.types.entity.MessageStatus;
import se.skltp.mb.types.services.TimeService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:services-config.xml" })
public class MessageMetaRepositoryTest extends JpaRepositoryTestBase {

    @Autowired
    private MessageMetaRepository messageRepository;

    @Autowired
    private TimeService timeService;

    @PersistenceContext
    EntityManager entityManager;

    private String corrId = "correlationId";

    @Test
    public void testPersist() throws Exception {
        messageRepository.create("sourceId", "hsaId", "orgId", "serviceContrakt", "webcall body", corrId);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_META", Long.class));
        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_BODY", Long.class));
    }

    @Test
    public void testListMessages() throws Exception {
        String targetSystem = "targetSystemHsaId";
        messageRepository.create("sourceId", targetSystem, "orgId", "serviceContrakt", "webcall body", corrId);

        entityManager.flush();
        entityManager.clear();

        List<MessageMeta> messages = messageRepository.listMessages(targetSystem);

        assertEquals(1, messages.size());
        assertNull(messages.get(0).getMessageBody());
    }

    @Test
    public void testGetMessages() throws Exception {
        Set<Long> ids = new HashSet<Long>();
        String targetSystem = "targetSystemHsaId";
        MessageMeta message = messageRepository.create("sourceId", targetSystem, "orgId", "serviceContrakt", "webcall body", corrId);
        ids.add(message.getId());

        entityManager.flush();
        entityManager.clear();

        List<MessageMeta> messages = messageRepository.getMessages(targetSystem, ids);

        assertEquals(1, messages.size());
        assertNotNull(messages.get(0).getMessageBody());
    }

    @Test
    public void testFindNone() throws Exception {
        Set<Long> ids = new HashSet<Long>();
        ids.add(1L);
        String targetSystem = "targetSys";
        List<MessageMeta> messages = messageRepository.getMessages(targetSystem, ids);
        assertEquals(0, messages.size());
    }

    @Test
    public void testOrdering() throws Exception {
        Set<Long> ids = new HashSet<Long>();
        String targetSystem = "targetSys";
        for(int i = 0 ; i < 5 ; i++) {
            MessageMeta message = messageRepository.create("sourceId", targetSystem, "org-1", "serviceContract", "webcallcontent", corrId);
            entityManager.persist(message);
            ids.add(message.getId());
        }
        
        entityManager.flush();
        entityManager.clear();
        
        List<MessageMeta> messages = messageRepository.getMessages(targetSystem, ids);
        
        int first = messages.get(0).getId().intValue();
        assertEquals(first + 1, messages.get(1).getId().intValue());
        assertEquals(first + 2, messages.get(2).getId().intValue());
        assertEquals(first + 3, messages.get(3).getId().intValue());
        assertEquals(first + 4, messages.get(4).getId().intValue());
    }


    @Test
    public void testDeleteFailDueToWrongStatus() throws Exception {
        String systemId = "hsaId";
        Set<Long> ids = createMessage(systemId);

        // fail delete because of wrong message status
        messageRepository.deleteMessages(systemId, ids);

        entityManager.flush();

        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_META", Long.class));
        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_BODY", Long.class));

    }

    @Test
    public void testDeleteWithCorrectStatus() throws Exception {
        String targetSystem = "hsaId";
        Set<Long> ids = createMessage(targetSystem);

        List<MessageMeta> messages = messageRepository.getMessages(targetSystem, ids);

        assertEquals(1, messages.size());
        messages.get(0).setStatusRetrieved();   // mark as retrived so we can delete it

        messageRepository.deleteMessages(targetSystem, ids);
        entityManager.flush();

        assertEquals(0, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_META", Long.class));
        assertEquals(0, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_BODY", Long.class));
    }

    @Test
    public void testDeleteBodiesWithCorrectStatus() throws Exception {
        String targetSystem = "hsaId";
        Set<Long> ids = createMessage(targetSystem);

        List<MessageMeta> messages = messageRepository.getMessages(targetSystem, ids);

        assertEquals(1, messages.size());
        messages.get(0).setStatusRetrieved();   // mark as retrived so we can delete it

        messageRepository.deleteMessageBodies(targetSystem, ids);
        entityManager.flush();

        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_META", Long.class));
        assertEquals(0, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_BODY", Long.class));
    }

    @Test
    public void testStatusReporting() throws Exception {
        // Create a bunch of messages with various systems, organizations, serviceContracts and times
        String ts1 = "hsaId1";
        String ts2 = "hsaId2";
        String org1 = "org1";
        String org2 = "org2";
        String org3 = "org3";
        String sc1 = "serviceContract1";
        String sc2 = "serviceContract2";
        String body = "webcall body";
        Date time1 = new Date(timeService.now() - 3600 * 1000);
        Date time2 = new Date(timeService.now() - 3600 * 1000 * 2);
        Date time3 = new Date(timeService.now() - 3600 * 1000 * 3);

        String srcId = "sourceId";
        messageRepository.create(srcId, ts2, org2, sc1, body, corrId, MessageStatus.RETRIEVED, time2);
        messageRepository.create(srcId, ts1, org1, sc1, body, corrId, MessageStatus.RECEIVED, time3);
        messageRepository.create(srcId, ts1, org1, sc2, body, corrId, MessageStatus.RECEIVED, time2);
        messageRepository.create(srcId, ts2, org3, sc1, body, corrId, MessageStatus.RECEIVED, time2);
        messageRepository.create(srcId, ts1, org1, sc1, body, corrId, MessageStatus.RETRIEVED, time1);
        messageRepository.create(srcId, ts2, org3, sc2, body, corrId, MessageStatus.RECEIVED, time1);
        messageRepository.create(srcId, ts2, org3, sc2, body, corrId, MessageStatus.RECEIVED, time3);

        // Get the status reports. The status reports are sorted, so we know what each slot in the
        // list should contain.
        List<StatusReport> reports = messageRepository.getStatusReports();
        assertEquals(5, reports.size());

        // system, org and service contracts are 1/1/1, 2 messages and oldest time is time3
        // 1/1/1, 2 msg, time3
        StatusReport sr = reports.get(0);
        assertEquals(ts1, sr.getTargetSystem());
        assertEquals(org1, sr.getTargetOrganization());
        assertEquals(sc1, sr.getServiceContract());
        assertEquals(2, sr.getMessageCount());
        assertEquals(time3, sr.getOldestMessageDate());
        assertEquals(1, sr.getRetrievedCount());

        // 1/1/2, 1 msg, time2
        sr = reports.get(1);
        assertEquals(ts1, sr.getTargetSystem());
        assertEquals(org1, sr.getTargetOrganization());
        assertEquals(sc2, sr.getServiceContract());
        assertEquals(1, sr.getMessageCount());
        assertEquals(time2, sr.getOldestMessageDate());
        assertEquals(0, sr.getRetrievedCount());

        // 2/2/1, 1 msg, time2
        sr = reports.get(2);
        assertEquals(ts2, sr.getTargetSystem());
        assertEquals(org2, sr.getTargetOrganization());
        assertEquals(sc1, sr.getServiceContract());
        assertEquals(1, sr.getMessageCount());
        assertEquals(time2, sr.getOldestMessageDate());
        assertEquals(1, sr.getRetrievedCount());

        // 2/3/1, 1 msg, time2
        sr = reports.get(3);
        assertEquals(ts2, sr.getTargetSystem());
        assertEquals(org3, sr.getTargetOrganization());
        assertEquals(sc1, sr.getServiceContract());
        assertEquals(1, sr.getMessageCount());
        assertEquals(time2, sr.getOldestMessageDate());
        assertEquals(0, sr.getRetrievedCount());

        // 2/3/2, 2 msg, time3
        sr = reports.get(4);
        assertEquals(ts2, sr.getTargetSystem());
        assertEquals(org3, sr.getTargetOrganization());
        assertEquals(sc2, sr.getServiceContract());
        assertEquals(2, sr.getMessageCount());
        assertEquals(time3, sr.getOldestMessageDate());
        assertEquals(0, sr.getRetrievedCount());

    }

    private Set<Long> createMessage(String systemId) {
        MessageMeta message = messageRepository.create("sourceId", systemId, "orgId", "serviceContrakt", "webcall body", corrId);

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_META", Long.class));
        assertEquals(1, (long) jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MESSAGE_BODY", Long.class));

        Set<Long> ids = new HashSet<Long>();
        ids.add(message.getId());
        return ids;
    }

}
