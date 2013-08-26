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
package se.inera.ifv.casebox.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

@NamedQueries({
        @NamedQuery(name="Answer.findAllForCareUnit", 
                    query="select a from Answer a where a.careUnit = :careUnit order by a.id asc"),
        @NamedQuery(name = "Answer.totalCountForCareUnit",
                    query = "select count(*) from Answer a where a.careUnit = :careUnit"),     
        @NamedQuery(name = "Answer.deleteForCareUnitWithIds", 
                    query = "delete from Answer a where a.careUnit = :careUnit and a.id in (:ids) and a.status = :status")                    
})
@Entity
@Table(name="ANSWER")
public class Answer extends AbstractEntity<Answer, Long> {

    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(name = "ARRIVED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrived;

    @Column(name = "CARE_UNIT")
    private String careUnit;

    @Column(name = "MESSAGE", nullable = false)
    @Lob
    private Serializable message;

    /* Make JPA happy */
    protected Answer() {
    }

    public Answer(String careUnit, Serializable message) {
        this(MessageStatus.RECEIVED, new Date(), careUnit, message);
    }

    public Answer(MessageStatus status, Date arrived, String careUnit, Serializable message) {
        this.status = status;
        this.arrived = arrived;
        this.careUnit = careUnit;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public Date getArrived() {
        return (Date) arrived.clone();
    }

    public String getCareUnit() {
        return careUnit;
    }

    public Serializable getMessage() {
        return message;
    }

    public void setStatusRetrieved() {
        this.status = MessageStatus.RETRIEVED;
    }
}
