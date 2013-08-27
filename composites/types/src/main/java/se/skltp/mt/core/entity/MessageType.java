package se.skltp.mt.core.entity;

/**
 * The type of the message that is handled in the casebox system.
 */
public enum MessageType {
    
    Answer("Answer"),
    Question("Question");

    private String nice;
    
    private MessageType(String nice) {
        this.nice = nice;
    }
    
    @Override
    public String toString() {
        return nice;
    }
}
