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
package se.skltp.messagebox.core.entity;

import java.util.Calendar;
import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Log statistics information about retrieved messages per targetSystem
 *
 * @author mats.olsson@callistaenterprise.se
 */
@NamedQueries({
        @NamedQuery(name = "Statistic.getForTargetSystemAndDay",
                query = "select s from Statistic s where s.targetSystem = :targetSystem and s.canonicalDayTime = :time"),
        @NamedQuery(name = "Statistic.getForTimeSlice",
                query = "select s from Statistic s "
                        + "where s.canonicalDayTime >= :startTime and s.canonicalDayTime <= :endTime "
                        + "order by s.targetSystem, s.targetOrganization, s.serviceContract, s.canonicalDayTime")

})
@Entity
@Table(name = "STATISTIC")
public class Statistic extends AbstractEntity<Long> {

    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The key is the canonical time for the day of the transactions logged,
     * according to {@link #getCanonicalDayTime}
     */
    @Column(nullable = false)
    private Long canonicalDayTime;

    // the target system of this message
    @Column(nullable = false)
    private String targetSystem;

    // the actual target org for a message; owned by the target system
    @Column(nullable = false)
    private String targetOrganization;

    // the service contract
    @Column(nullable = false)
    private String serviceContract;

    // number of <serviceContract> messages delivered (read and deleted) for <targetSystem>
    @Column(nullable = false)
    private int deliveryCount;

    // total size of message bodies delivered
    @Column(nullable = false)
    private long totalSize;

    // total number of milliseconds that the messages have waited
    private long totalWaitTimeMs;

    // maximum time a message waited before being delivered
    private long maxWaitTimeMs;

    protected Statistic() {
    }

    /**
     * Construct a statistic entry for the (receiver, service contract, day) tuple
     *
     * @param targetSystem       target system id
     * @param targetOrganization the target org of the message
     * @param serviceContract    service contract
     * @param time               will be converted to canonical day time
     */
    public Statistic(String targetSystem, String targetOrganization, String serviceContract, long time) {
        this.targetSystem = targetSystem;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.canonicalDayTime = convertToCanonicalDayTime(time);
        this.totalSize = 0;
        this.deliveryCount = 0;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", canonicalDayTime=" + canonicalDayTime +
                ", targetSystem='" + targetSystem + '\'' +
                ", targetOrganization='" + targetOrganization + '\'' +
                ", serviceContract='" + serviceContract + '\'' +
                ", deliveryCount=" + deliveryCount +
                ", totalSize=" + totalSize +
                ", totalWaitTimeMs=" + totalWaitTimeMs +
                ", maxWaitTimeMs=" + maxWaitTimeMs +
                '}';
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getCanonicalDayTime() {
        return canonicalDayTime;
    }


    public String getTargetSystem() {
        return targetSystem;
    }

    public String getServiceContract() {
        return serviceContract;
    }

    public int getDeliveryCount() {
        return deliveryCount;
    }

    /**
     * One message has been delivered for this (receiver,sk,day) tuple, and it spent waitTimeMs waiting.
     *
     * @param messageBodySize the size of the message body
     * @param waitTimeMs      time between arrival and
     */
    public void addDelivery(long messageBodySize, long waitTimeMs) {
        totalSize += messageBodySize;
        deliveryCount++;
        maxWaitTimeMs = Math.max(maxWaitTimeMs, waitTimeMs);
        totalWaitTimeMs += waitTimeMs;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getTotalWaitTimeMs() {
        return totalWaitTimeMs;
    }

    public long getMaxWaitTimeMs() {
        return maxWaitTimeMs;
    }

    public String getTargetOrganization() {
        return targetOrganization;
    }


    /**
     * Return the canonical day time for the day indicated by time.
     * <p/>
     * Used to find the key for the statistics entry for a given time.
     * <p/>
     * Strips out the ms/s/min/hour part of the time.
     *
     * @param time to convert
     * @return the time stripped down to the start of the day
     */
    public static long convertToCanonicalDayTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        return cal.getTimeInMillis();
    }

}
