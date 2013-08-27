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

package se.skltp.mt.core.service.impl;

import se.skltp.mt.core.entity.MessageStatus;
import se.skltp.mt.core.service.CareUnitInfo;

/**
 * @author Pär Wenåker
 *
 */
public abstract class CareUnitInfoInternal implements Comparable<CareUnitInfoInternal>{

    private String name;
    private String type;
    private MessageStatus status;
    private long count;
    
    public CareUnitInfoInternal(String name, String type, MessageStatus status, long count) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.count = count;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @return the status
     */
    public MessageStatus getStatus() {
        return status;
    }
    
    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    public abstract void update(CareUnitInfo info);
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(CareUnitInfoInternal o) {
        if(this.name == o.name) {
            return this.status.compareTo(o.status);
        } 
        return this.name.compareTo(o.name);
    }

}
