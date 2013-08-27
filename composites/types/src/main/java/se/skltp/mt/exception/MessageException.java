package se.skltp.mt.exception;

/**
 * Service exception for casebox. Thrown up to userinterface to translate error messages. 
 */
public class MessageException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6668568019292172346L;
    private String messageCode;
    
    public MessageException(String messageCode) {
        this.messageCode = messageCode;
    }
    
    public String getMessageCode() {
        return messageCode;
    }
}
