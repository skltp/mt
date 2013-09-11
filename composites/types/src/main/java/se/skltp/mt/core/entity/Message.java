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
package se.skltp.mt.core.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import se.skltp.riv.itintegration.messagebox.v1.MessageStatusType;
import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Entity representing a generic message.
 * 
 * This wraps the content of an incoming web-service call.
 * 
 * @author mats.olsson@callistaenterprise.se
 */
/*
@NamedQueries({
	@NamedQuery(name = "Question.findAllForCareUnit", 
		    query = "select q from Question q where q.careUnit = :careUnit order by q.id asc"),
	@NamedQuery(name = "Question.deleteForCareUnitWithIds", 
		    query = "delete from Question q where q.careUnit = :careUnit and q.id in (:ids) and q.status = :status"),
	@NamedQuery(name = "Question.totalCountForCareUnit",
	            query = "select count(*) from Question q where q.careUnit = :careUnit")
})
*/
@Entity()
@Table(name="MESSAGE")
public class Message extends AbstractEntity<Long> {

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(nullable=false, name="STATUS")
    @Enumerated(EnumType.STRING)
    private MessageStatusType status;

    @Column(name="ARRIVED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrived;
    
    @Column(name="SYSTEM_ID")
    private String systemId;
    
    @Column(nullable=false, name="MESSAGE")
    @Lob
    private Serializable messageBody;

    @Column(nullable=false, name="SERVICE_CONTRACT")
    private String serviceContract;

    /* Make JPA happy */
    protected Message() {}

    public Message(String systemId, String serviceContract, Serializable message) {
        this(MessageStatusType.RECEIVED, new Date(), systemId, serviceContract, message);
    }


    public Message(MessageStatusType status, Date arrived, String systemId, String serviceContract, Serializable message) {
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

    public Serializable getMessageBody() {
        return messageBody;
    }
    
    public void setStatusRetrieved() {
    	this.status = MessageStatusType.RETRIEVED;
    }

    public String getServiceContract() {
        return serviceContract;
    }

}
