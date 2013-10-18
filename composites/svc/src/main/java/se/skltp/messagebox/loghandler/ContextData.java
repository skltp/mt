package se.skltp.messagebox.loghandler;

public class ContextData {

	public String correlationId;
	public String messageId;
	
	public ContextData(String correlationId, String messageId) {
		this.correlationId = correlationId;
		this.messageId = messageId;
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
	
}
