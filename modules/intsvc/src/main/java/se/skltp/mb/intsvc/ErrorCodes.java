package se.skltp.mb.intsvc;

/**
 * Error codes for List/Get/Delete
 *
 * @author mats.olsson@callistaenterprise.se
 */
public enum ErrorCodes {

    OK("Not an error"), // to get the ordinal() right - zero is not an error
    INTERNAL("Internal error"),
    UNREAD_DELETE("Attempted to delete unread messages");

    private String text;

    ErrorCodes(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ErrorCode." + name() + ":" + text;
    }

    public String getText() {
        return text;
    }
}
