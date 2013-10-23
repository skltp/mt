package se.skltp.messagebox.intsvc;

/**
 * Error codes for List/Get/Delete
 *
 * @author mats.olsson@callistaenterprise.se
 */
public enum ErrorCode {

    OK("Not an error"), // to get the ordinal() right - zero is not an error
    INTERNAL("Internal error"),
    UNREAD_DELETE("Attempted to delete unread messages");

    private String text;

    ErrorCode(String text) {
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
