package se.skltp.messagebox.core.entity;

/**
 * This should match the MessageStatusType from the schema package.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public enum MessageStatus {
    RECEIVED,
    RETRIEVED,
    DELETED;

    public String value() {
        return name();
    }

    public static MessageStatus fromValue(String v) {
        return valueOf(v);
    }
}
