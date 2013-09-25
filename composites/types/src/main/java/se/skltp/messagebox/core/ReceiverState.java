package se.skltp.messagebox.core;

import java.util.Date;

/**
 * Describes the state of a receiving system.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class ReceiverState {
    private String receivingSystem;
    private long numberOfMessages;
    private Date oldestMessage;

    public ReceiverState(String receivingSystem, long numberOfMessages, Date oldestMessage) {
        this.receivingSystem = receivingSystem;
        this.numberOfMessages = numberOfMessages;
        this.oldestMessage = oldestMessage;
    }

    public String getReceivingSystem() {
        return receivingSystem;
    }

    public long getNumberOfMessages() {
        return numberOfMessages;
    }


    public Date getOldestMessage() {
        return oldestMessage;
    }
}
