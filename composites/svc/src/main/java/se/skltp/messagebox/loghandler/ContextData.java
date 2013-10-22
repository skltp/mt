package se.skltp.messagebox.loghandler;

public class ContextData {

	public String correlationId;
    public String originalCorrelationId;
    public String messageId;

    public ContextData(String correlationId, String originalCorrelationId, String messageId) {
		this.correlationId = correlationId;
		this.messageId = messageId;
        this.originalCorrelationId = originalCorrelationId;
    }

	public ContextData() {}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

    public String getOriginalCorrelationId() {
        return originalCorrelationId;
    }

    public void setOriginalCorrelationId(String originalCorrelationId) {
        this.originalCorrelationId = originalCorrelationId;
    }
}
