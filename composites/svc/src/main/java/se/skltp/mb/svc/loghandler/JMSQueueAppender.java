package se.skltp.mb.svc.loghandler;
//

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.jms.*;
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

    private Connection connection;

    // Settings
    private String brokerURL;
    private String infoQueueName;
    private String errorQueueName;
    private String componentName;


    /**
     * Used by logging methods in {@link se.skltp.mb.svc.services} for pushing data to this class
     */
    private static final ThreadLocal<ContextData> contextData = new ThreadLocal<ContextData>() {

        protected ContextData initialValue() {
            return new ContextData();
        }

    };

    public static void setContextData(ContextData data) {
        if ( contextData != null ) {
            if ( data == null ) {
                contextData.remove();
            } else {
                contextData.set(data);
            }
        }
    }

    @Override
    public void close() {

        try {
            connection.close();
        } catch (JMSException e) {
        }
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
        if ( isLogEventFromThisClass(event) ) {
        	System.err.println("YYY: Got log message from myself: " + event.getMessage().toString());
            return;
        }
        
        System.err.println("XXX: Got log messsage: " + event.getMessage().toString());

        try {
            LogEvent logEvent = LogEventCreator.createLogEvent(event, contextData.get(), componentName);
            String queue = getQueueName(logEvent.getLogEntry().getMessageInfo().getLevel());

            logToQueue(queue, marshall(logEvent));

        } catch (Exception e) {
        	logger.warn("BBB: Could not log message to queue", e);
        }
    }


    /**
     * Send message to queue
     *
     * @param queue name of the queue that the message should be sent to
     * @param xml   the xml string
     */
    private void logToQueue(String queue, String xml) {

    	 Session session = null;
    	
        try {
            session = getSession();
            Destination dest;
            dest = session.createQueue(queue);
            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage msg = session.createTextMessage(xml);
            producer.send(msg);

            // Close session
            session.close();

        } catch (JMSException e) {
            // Force a new connection to be made on the next request
            if ( connection != null ) {
            	
            	try {
					connection.close();
				} catch (JMSException e1) {
				}
            	
                connection = null;
            }
        	
        	
            logger.error("AAA: Could not log to " + queue + "!", e);


        } finally {
        	try {
        		if (session != null ){
				session.close();
        		}
			} catch (JMSException e) {
				session = null;
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

        if ( connection == null ) {

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
        if ( this.getClass().getName().equals(event.getLoggerName()) ) {
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

        if ( errorLevels.contains(level) ) {
            return errorQueueName;
        } else {
            return infoQueueName;
        }
    }


    // Setters

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public void setInfoQueueName(String infoQueueName) {
        this.infoQueueName = infoQueueName;
    }

    public void setErrorQueueName(String errorQueueName) {
        this.errorQueueName = errorQueueName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }


}