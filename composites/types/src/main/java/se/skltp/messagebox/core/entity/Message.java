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

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Entity representing a generic message.
 * <p/>
 * This wraps the content of an incoming web-service call.
 *
 * @author mats.olsson@callistaenterprise.se
 */
@NamedQueries({
        @NamedQuery(name = "Message.getForReceiver",
                query = "select m from Message m where m.targetSystem = :systemId order by m.id asc"),
        @NamedQuery(name = "Message.getForReceiverWithIds",
                query = "select m from Message m where m.targetSystem = :systemId and m.id in (:ids) order by m.id asc"),
        @NamedQuery(name = "Message.deleteForReceiverWithIdsAndStatus",
                query = "delete from Message m where m.targetSystem = :systemId and m.id in (:ids) and m.status = :status"),
        @NamedQuery(name = "Message.totalCountForReceiver",
                query = "select count(m) from Message m where m.targetOrganization = :systemId"),
        @NamedQuery(name = "Message.receiverStates",
                query = "select m.targetSystem, count(m.targetSystem), min(m.arrived) from Message m GROUP BY m.targetSystem")

})
@Entity()
@Table(name = "MESSAGE")
public class Message extends AbstractEntity<Long> {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The hsaId of the source system of this message; used for error tracking/logging.
    // extracted from http-header "x-rivta-original-serviceconsumer-hsaid"
    private String sourceSystem;

    // the hsaId for the receiving system of this message; it must authenticate using this id to list/get/delete
    @Column(nullable = false)
    private String targetSystem;

    // the hsaId for the target organization (verksamhetsId)
    @Column(nullable = false)
    private String targetOrganization;

    // the service contract - extracted from messageBody
    @Column(nullable = false)
    private String serviceContract;

    // The correlation id (VP message id) of the received message. Also only used for error tracking/logging
    private String businessCorrelationId;

    @Column(nullable = false)
    @Lob
    private String messageBody;

    // the size of the messagebody
    @Column(nullable = false)
    private long messageBodySize;

    //
    // possible optimization for large messages would be to split messageBody into
    // smallMessageBody and largeMessageBody, with largeMessageBody being marked as
    // lazy-loaded.
    //
    // The size of the message body would be used to determine where the message body
    // is actually stored (when reading, if smallMessageBody is null, then you have
    // to read the largeMessageBody)
    //
    //    @Basic(fetch = FetchType.LAZY)
    //    @Column(nullable = true)
    //    @Lob
    //    private String largeMessageBody;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrived;

    /* Make JPA happy */
    protected Message() {
    }


    public Message(String sourceSystem, String targetSystem, String targetOrganization, String serviceContract, String messageBody, MessageStatus status, Date arrived, String correlationId) {
        this.sourceSystem = sourceSystem;
        this.targetSystem = targetSystem;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.messageBody = messageBody;
        this.messageBodySize = messageBody.length();
        this.status = status;
        this.arrived = arrived;
        this.businessCorrelationId = correlationId;
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

    public String getMessageBody() {
        return messageBody;
    }

    public String getServiceContract() {
        return serviceContract;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public String getBusinessCorrelationId() {
        return businessCorrelationId;
    }

    public long getMessageBodySize() {
        return messageBodySize;
    }

    /**
     * Must be called before the message can be deleted.
     */
    public void setStatusRetrieved() {
        this.status = MessageStatus.RETRIEVED;
    }

}
