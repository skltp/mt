package se.skltp.messagebox.svc.exception;

import java.util.List;

import se.skltp.messagebox.types.entity.MessageMeta;

/**
 * Exception thrown when an attempt is made to delete unread messages
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class UnreadDeleteException extends Exception {
    private List<MessageMeta> unreadMessages;

    public UnreadDeleteException(List<MessageMeta> unreadMessages) {
        super("Attempting to delete unread messages!");
        this.unreadMessages = unreadMessages;
    }

    public List<MessageMeta> getUnreadMessages() {
        return unreadMessages;
    }

    public String getUnreadIdsAsCsv() {
        StringBuilder sb = new StringBuilder();
        for ( MessageMeta msg : unreadMessages ) {
            if ( sb.length() > 0 ) {
                sb.append(",");
            }
            sb.append(msg.getId());
        }
        return sb.toString();
    }


}
