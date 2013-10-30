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
package se.skltp.mb.types.entity;

import java.util.Date;
import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Meta-data about a message.
 *
 * To avoid loading the message body (which can be large) we
 *
 *
 * @author mats.olsson@callistaenterprise.se
 */
@NamedQueries({
        @NamedQuery(name = "Message.listAllMessages",
                query = "select m from MessageMeta m where m.targetSystem = :targetSystem order by m.id asc"),
        @NamedQuery(name = "Message.listSomeMessages",
                query = "select m from MessageMeta m where m.targetSystem = :targetSystem and m.id in (:ids) order by m.id asc"),
        @NamedQuery(name = "Message.deleteMessages",
                query = "delete from MessageMeta m where m.targetSystem = :targetSystem and m.id in (:ids) and m.status = :status"),
        @NamedQuery(name = "Message.getStatus",
                query = "select m.targetSystem, count(m.targetSystem), min(m.arrived) from MessageMeta m GROUP BY m.targetSystem")

})
@Entity()
@Table(name = "message_meta")
public class MessageMeta extends AbstractEntity<Long> {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The hsaId of the source system of this message; used for error tracking/logging.
    // extracted from http-header "x-rivta-original-serviceconsumer-hsaid"
    private String sourceSystem;

    // the hsaId for the target system of this message; it must authenticate using this id to list/get/delete
    @Column(nullable = false)
    private String targetSystem;

    // the hsaId for the target organization (verksamhetsId)
    @Column(nullable = false)
    private String targetOrganization;

    // the service contract - extracted from messageBody
    @Column(nullable = false)
    private String serviceContract;

    // the size of the messagebody
    @Column(nullable = false)
    private long messageBodySize;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrived;

    // maps to X-MULE-CORRELATION-ID; used to allow mule-people to match this message against the mule-logs
    private String correlationId;

    // Can't trust hibernate NOT to load the message body even if we use lazy annotation, so we disable automatic
    // loading of the messageBody. It will be filled in only if you do a getMessages() field; if used when doing
    // listMessages it will be null.
    // Note that this makes it possible to have "garbage" MessageBodies lying around if you forget to delete them manually -
    // the built-in "cascade" relationship can't be used.
    private transient MessageBody messageBody;


    /* Make JPA happy */
    protected MessageMeta() {
    }

    public MessageMeta(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, MessageBody messageBody, String correlationId, MessageStatus status, Date arrived) {
        this.sourceSystem = sourceSystem;
        this.targetSystem = targetSystem;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.messageBodySize = messageBody.getText().length();
        this.status = status;
        this.arrived = arrived;
        this.correlationId = correlationId;
        this.messageBody = messageBody;
    }

    public Long getId() {
        return id;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public Date getArrived() {
        return (Date) arrived.clone();
    }

    public String getTargetOrganization() {
        return targetOrganization;
    }

    public String getServiceContract() {
        return serviceContract;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public long getMessageBodySize() {
        return messageBodySize;
    }

    /**
     * Business correlation id used for tracing messages in surrounding environment.
     *
     * @return a string useful for logging
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Must be called before the message can be deleted.
     */
    public void setStatusRetrieved() {
        this.status = MessageStatus.RETRIEVED;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }
}
