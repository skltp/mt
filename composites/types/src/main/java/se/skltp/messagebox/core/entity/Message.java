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
package se.skltp.messagebox.core.entity;

import java.util.Date;
import javax.persistence.*;

import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;
import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Entity representing a generic message.
 * <p/>
 * This wraps the content of an incoming web-service call.
 *
 * @author mats.olsson@callistaenterprise.se
 */
@NamedQueries({
        @NamedQuery(name = "Message.getForSystem",
                query = "select m from Message m where m.systemId = :systemId order by m.id asc"),
        @NamedQuery(name = "Message.getForSystemWithIds",
                query = "select m from Message m where m.systemId = :systemId and m.id in (:ids) order by m.id asc"),
        @NamedQuery(name = "Message.deleteForSystemWithIds",
                query = "delete from Message m where m.systemId = :systemId and m.id in (:ids)"),
        // TODO: how to delete? with or without status?
        @NamedQuery(name = "Message.deleteForSystemWithIdsAndStatus",
                query = "delete from Message m where m.systemId = :systemId and m.id in (:ids) and m.status = :status"),
        @NamedQuery(name = "Message.totalCountForSystem",
                query = "select count(m) from Message m where m.systemId = :systemId")
})
@Entity()
public class Message extends AbstractEntity<Long> {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatusType status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrived;

    private String systemId;

    @Column(nullable = false)
    @Lob
    private String messageBody;

    // the service contrakt - extracted from messageBody
    @Column(nullable = false)
    private String serviceContract;

    /* Make JPA happy */
    protected Message() {
    }

    public Message(String systemId, String serviceContract, String message) {
        this(MessageStatusType.RECEIVED, new Date(), systemId, serviceContract, message);
    }


    public Message(MessageStatusType status, Date arrived, String systemId, String serviceContract, String message) {
        this.status = status;
        this.arrived = arrived;
        this.systemId = systemId;
        this.serviceContract = serviceContract;
        this.messageBody = message;
    }

    public Long getId() {
        return id;
    }

    public MessageStatusType getStatus() {
        return status;
    }

    public Date getArrived() {
        return (Date) arrived.clone();
    }

    public String getSystemId() {
        return systemId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setStatusRetrieved() {
        this.status = MessageStatusType.RETRIEVED;
    }

    public String getServiceContract() {
        return serviceContract;
    }

}
