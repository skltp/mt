package se.skltp.messagebox.core;

import java.util.Date;

import se.skltp.messagebox.TimeDelta;

/**
 * A status-report for a (reciver/targetOrg/service contract) tuple.
 * <p/>
 * A list of these can be used as a full report for the system
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class StatusReport implements Comparable<StatusReport> {
    private String receiver;
    private String targetOrganization;
    private String serviceContract;
    private long messageCount;
    private Date oldestMessageDate;

    public StatusReport(String receiver, String targetOrganization, String serviceContract, long messageCount, Date oldestMessageDate) {
        this.receiver = receiver;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.messageCount = messageCount;
        this.oldestMessageDate = oldestMessageDate;
    }

    public String getReceiver() {
        return receiver;
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

    public TimeDelta getOldestMessageAge() {
        return new TimeDelta(System.currentTimeMillis() - oldestMessageDate.getTime());
    }


    @Override
    public boolean equals(Object o) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        StatusReport that = (StatusReport) o;

        if ( receiver != null ? !receiver.equals(that.receiver) : that.receiver != null )
            return false;
        if ( serviceContract != null ? !serviceContract.equals(that.serviceContract) : that.serviceContract != null )
            return false;
        if ( targetOrganization != null ? !targetOrganization.equals(that.targetOrganization) : that.targetOrganization != null )
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = receiver != null ? receiver.hashCode() : 0;
        result = 31 * result + (targetOrganization != null ? targetOrganization.hashCode() : 0);
        result = 31 * result + (serviceContract != null ? serviceContract.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(StatusReport o) {
        int n;
        if ( (n = receiver.compareTo(o.receiver)) != 0 ) {
            return n;
        }
        if ( (n = targetOrganization.compareTo(o.targetOrganization)) != 0 ) {
            return n;
        }
        return serviceContract.compareTo(o.serviceContract);
    }

}