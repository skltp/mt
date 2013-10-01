package se.skltp.messagebox.core.entity;

import java.util.Calendar;
import javax.persistence.*;

import se.vgregion.dao.domain.patterns.entity.AbstractEntity;

/**
 * Log statistics information about retrieved messages per receiverId
 *
 * @author mats.olsson@callistaenterprise.se
 */
@NamedQueries({
        @NamedQuery(name = "Statistic.getForReceiverAndDayTime",
                query = "select s from Statistic s where s.receiverId = :receiverId and s.canonicalDayTime = :time"),
        @NamedQuery(name = "Statistic.getForTimeSlice",
                query = "select s from Statistic s "
                + "where s.canonicalDayTime >= :startTime and s.canonicalDayTime <= :endTime "
                + "order by s.receiverId, s.targetOrganization, s.serviceContract, s.canonicalDayTime")

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

    // the hsaId for the receiver of this message; the calling system must authenticate using this id
    @Column(nullable = false)
    private String receiverId;

    // the actual target org for a message; owned by the receiving system
    @Column(nullable = false)
    private String targetOrganization;

    // the service contract
    @Column(nullable = false)
    private String serviceContract;

    // number of <serviceContract> messages delivered (read and deleted) for <receiverId>
    @Column(nullable = false)
    private int deliveryCount;

    // total number of milliseconds that the messages have waited
    private long totalWaitTimeMs;

    // maximum time a message waited before being delivered
    private long maxWaitTimeMs;

    protected Statistic() {
    }

    /**
     * Construct a statistic entry for the (receiver, service contract, day) tuple
     *
     * @param receiverId         receiving system id
     * @param targetOrganization the target org of the message
     * @param serviceContract    service contract
     * @param time               will be converted to canonical day time
     */
    public Statistic(String receiverId, String targetOrganization, String serviceContract, long time) {
        this.receiverId = receiverId;
        this.targetOrganization = targetOrganization;
        this.serviceContract = serviceContract;
        this.canonicalDayTime = convertToCanonicalDayTime(time);
        this.deliveryCount = 0;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Long getCanonicalDayTime() {
        return canonicalDayTime;
    }


    public String getReceiverId() {
        return receiverId;
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
     * @param waitTimeMs time between arrival and
     */
    public void addDelivery(long waitTimeMs) {
        deliveryCount++;
        maxWaitTimeMs = Math.max(maxWaitTimeMs, waitTimeMs);
        totalWaitTimeMs += waitTimeMs;
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
