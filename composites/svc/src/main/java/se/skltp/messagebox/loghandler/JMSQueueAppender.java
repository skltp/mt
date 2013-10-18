package se.skltp.messagebox.loghandler;
//
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogEvent;
import org.soitoolkit.commons.logentry.schema.v1.LogLevelType;


public class JMSQueueAppender extends AppenderSkeleton implements Appender {

	
	private static Logger logger = Logger.getLogger(JMSQueueAppender.class);
	
	private static final ArrayList<LogLevelType> infoLevels = new ArrayList<LogLevelType>(Arrays.asList(LogLevelType.INFO, LogLevelType.DEBUG));
	private static final ArrayList<LogLevelType> errorLevels = new ArrayList<LogLevelType>(Arrays.asList(LogLevelType.ERROR, LogLevelType.WARNING));
	
	private String brokerURL = "tcp://localhost:61616";
	private Connection connection;
	
	private String infoQueueName = "MT_DEFAULT_INFO";
	private String errorQueueName = "MT_DEFAULT_ERROR";
	
	
	/**
	 * Used by logging methods in se.skltp.messagebox.services for pushing data to this class
	 */
	private static final ThreadLocal<ContextData> contextData = new ThreadLocal<ContextData>() {
		
		protected ContextData initialValue() {
			return new ContextData(); 
		}
		
	};
	
	public static void setContextData(ContextData data) {
		if(contextData != null) {
			contextData.set(data);
		}
	}
	
	@Override
	public void close() {

		try {
			connection.close();
		} catch (JMSException e) {}
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}


	/**
	 * Append event to queue
	 */
	protected void append(LoggingEvent event) {
		
		// TODO - This should be done in log4.properties
		// Do not process events that originates from this class
		if (isLogEventFromThisClass(event)) {
			return;
		}

		try {
			LogEvent logEvent = LogEventCreator.createLogEvent(event, contextData.get());
			String queue = getQueueName(logEvent.getLogEntry().getMessageInfo().getLevel());
			
			logToQueue(queue, marshall(logEvent));
			
		} catch (Exception e) {
			logger.warn("Could not log message to queue", e);
		}
	}


	
	/**
	 * Send message to queue
	 * 
	 * @param queue name of the queue that the message should be sent to
	 * @param xml the xml string
	 */
	private void logToQueue(String queue, String xml) {
		
		try {
			Session session = getSession();
			Destination dest;
			dest = session.createQueue(queue);
			MessageProducer producer = session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); 
			
			TextMessage msg = session.createTextMessage(xml);
			producer.send(msg);

			// Close session
			session.close();
		
		} catch (JMSException e) {
			logger.error("Could not log to " + queue + "!", e);
			
			// Force a new connection to be made on the next request
			if(connection != null) {
				connection = null;
			}
		}
	}
	
	/**
	 * Retrieve a session 
	 * 
	 * @return a new session
	 * @throws JMSException 
	 */
	private Session getSession() throws JMSException {
		
		if(connection == null) {

			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);

			connection = connectionFactory.createConnection();
			connection.start();
		}

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		return session;
	}
	
	/**
	 * Convert the LogEvent object to a string
	 * 
	 * @param logEvent
	 * @return
	 * @throws JAXBException
	 */
	private String marshall(LogEvent logEvent) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(LogEvent.class);
		
		StringWriter writer = new StringWriter();
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(logEvent, writer);
		
		return writer.toString();
	}
	
	
	/**
	 * Check whether the (log) event originated from this class.
	 * 
	 * @param event
	 * @return true if the event originated from this class
	 */
	private boolean isLogEventFromThisClass(LoggingEvent event) {
		if (this.getClass().getName().equals(event.getLoggerName())) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Get the corresponding queue (name) for this log level
	 * 
	 * @param level
	 * @return the queue name 
	 */
	private String getQueueName(LogLevelType level) {
	
		if(errorLevels.contains(level)) {
			return errorQueueName;
		} else {
			return infoQueueName;
		}
	}
	
	
	// Getter/setters
	
	public String getBrokerURL() {
		return brokerURL;
	}

	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}

	public String getInfoQueueName() {
		return infoQueueName;
	}

	public void setInfoQueueName(String infoQueueName) {
		this.infoQueueName = infoQueueName;
	}

	public String getErrorQueueName() {
		return errorQueueName;
	}

	public void setErrorQueueName(String errorQueueName) {
		this.errorQueueName = errorQueueName;
	}
	
	

}