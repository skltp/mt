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
        @NamedQuery(name = "Message.getForReceiver",
                query = "select m from Message m where m.receiverId = :systemId order by m.id asc"),
        @NamedQuery(name = "Message.getForReceiverWithIds",
                query = "select m from Message m where m.receiverId = :systemId and m.id in (:ids) order by m.id asc"),
        @NamedQuery(name = "Message.deleteForReceiverWithIdsAndStatus",
                query = "delete from Message m where m.receiverId = :systemId and m.id in (:ids) and m.status = :status"),
        @NamedQuery(name = "Message.totalCountForReceiver",
                query = "select count(m) from Message m where m.targetOrganization = :systemId"),
        @NamedQuery(name = "Message.receiverStates",
                query = "select m.receiverId, count(m.receiverId), min(m.arrived) from Message m GROUP BY m.receiverId")

})
@Entity()
@Table(name = "MESSAGE")
public class Message extends AbstractEntity<Long> {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // the hsaId for the target of this message; the calling system must authenticate using this id
    @Column(nullable = false)
    private String receiverId;

    // the hsaId for the target organization (verksamhetsId)
    @Column(nullable = false)
    private String targetOrganization;

    // the service contrakt - extracted from messageBody
    @Column(nullable = false)
    private String serviceContract;

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
    private MessageStatusType status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date arrived;

    /* Make JPA happy */
    protected Message() {
    }

    public Message(String receiverId, String targetOrganization, String serviceContract, String message) {
        this(receiverId,
                targetOrganization,
                serviceContract,
                message,
                MessageStatusType.RECEIVED,
                new Date(System.currentTimeMillis()));
    }


    public Message(String receiverId, String targetOrganization, String serviceContract, String messageBody, MessageStatusType status, Date arrived) {
        this.receiverId = receiverId;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.messageBody = messageBody;
        this.status = status;
        this.arrived = arrived;
    }

    public Long getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public MessageStatusType getStatus() {
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

    /**
     * Must be called before the message can be deleted.
     */
    public void setStatusRetrieved() {
        this.status = MessageStatusType.RETRIEVED;
    }

}
