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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Statistic entity that holds information about how many Questions and Answers 
 * the sytem has handled per day and per careunit.  
 */
@NamedQueries({
        @NamedQuery(name = "Statistic.find", 
                query = "select s from Statistic s where messageType = :messageType and careUnit = :careUnit and created = :created")
})
@Entity
@Table(name = "STATISTIC")
public class Statistic extends AbstractEntity<Statistic, Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "MESSAGE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "CARE_UNIT", nullable = false)
    private String careUnit;

    @Column(name = "NUMBER", nullable = false)
    private int number;

    /* Make JPA happy */
    protected Statistic() {
    }

    public Statistic(MessageType messageType, Date created, String careUnit, int number) {
        this.messageType = messageType;
        this.created = created;
        this.careUnit = careUnit;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCareUnit() {
        return careUnit;
    }

    public void setCareUnit(String careUnit) {
        this.careUnit = careUnit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
