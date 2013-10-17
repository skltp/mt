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
package se.skltp.messagebox.core;

import java.util.Date;

/**
 * A status-report for a (reciver/targetOrg/service contract) tuple.
 * <p/>
 * A list of these can be used as a full report for the system
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class StatusReport implements Comparable<StatusReport> {
    private String targetSystem;
    private String targetOrganization;
    private String serviceContract;
    private long messageCount;
    private Date oldestMessageDate;
    private long totalSize;

    public StatusReport(String targetSystem, String targetOrganization, String serviceContract, long messageCount, long totalSize, Date oldestMessageDate) {
        this.targetSystem = targetSystem;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.messageCount = messageCount;
        this.totalSize = totalSize;
        this.oldestMessageDate = oldestMessageDate;
    }

    public String getTargetSystem() {
        return targetSystem;
    }

    public String getTargetOrganization() {
        return targetOrganization;
    }

    public String getServiceContract() {
        return serviceContract;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public Date getOldestMessageDate() {
        return oldestMessageDate;
    }


    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        StatusReport that = (StatusReport) o;

        if ( targetSystem != null ? !targetSystem.equals(that.targetSystem) : that.targetSystem != null )
            return false;
        if ( serviceContract != null ? !serviceContract.equals(that.serviceContract) : that.serviceContract != null )
            return false;
        if ( targetOrganization != null ? !targetOrganization.equals(that.targetOrganization) : that.targetOrganization != null )
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = targetSystem != null ? targetSystem.hashCode() : 0;
        result = 31 * result + (targetOrganization != null ? targetOrganization.hashCode() : 0);
        result = 31 * result + (serviceContract != null ? serviceContract.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(StatusReport o) {
        int n;
        if ( (n = targetSystem.compareTo(o.targetSystem)) != 0 ) {
            return n;
        }
        if ( (n = targetOrganization.compareTo(o.targetOrganization)) != 0 ) {
            return n;
        }
        return serviceContract.compareTo(o.serviceContract);
    }

    public long getTotalSize() {
        return totalSize;
    }
}
