package se.skltp.mb.svc.loghandler;
//

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ConnectionFailedException;
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


    /**
     * Used by logging methods in {@link se.skltp.mb.svc.services} for pushing data to this class
     */
    private static final ThreadLocal<ContextData> contextData = new ThreadLocal<ContextData>() {

        protected ContextData initialValue() {
            return new ContextData();
        }

    };
    private ConfigBean config;

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

    private ConfigBean getConfig() {

        if ( config == null ) {
            try {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");
                config = (ConfigBean) envCtx.lookup("bean/MessageboxJmsConfig");
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return config;
    }

    public String getComponentName() {
        return getConfig().getComponentName();
    }

    public String getBrokerURL() {
        return getConfig().getBrokerURL();
    }

    public String getInfoQueueName() {
        return getConfig().getInfoQueueName();
    }

    public String getErrorQueueName() {
        return getConfig().getErrorQueueName();
    }

    /**
     * Append event to queue
     */
    protected void append(LoggingEvent event) {
        if ( !Boolean.parseBoolean(getConfig().getActive()) ) {
            return;
        }

        // Do not process events that originates from this class
        if ( isLogEventFromThisClass(event) ) {
            return;
        }

        try {
            LogEvent logEvent = LogEventCreator.createLogEvent(event, contextData.get(), getComponentName());
            String queue = getQueueName(logEvent.getLogEntry().getMessageInfo().getLevel());
            if ( queue != null ) {
                logToQueue(queue, marshall(logEvent));
            }

        } catch (Exception e) {
            logger.warn("Could not log message to queue", e);
        }
    }


    /**
     * Send message to queue
     *
     * @param queue name of the queue that the message should be sent to
     * @param xml   the xml string
     */
    private void logToQueue(String queue, String xml) {

        try {
            Session session = getSessionWithRetry();

            Destination dest;
            dest = session.createQueue(queue);
            MessageProducer producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage msg = session.createTextMessage(xml);
            producer.send(msg);

            // Close session
            session.close();

        } catch (JMSException e) {
            System.err.println("Could not log to " + queue + "! : " + e);
            e.printStackTrace();
            logger.error("Could not log to " + queue + "!", e);

            // Force a new connection to be made on the next request
            if ( connection != null ) {
                connection = null;
            }
        }
    }

    private Session getSessionWithRetry() throws JMSException {
        Session session = null;
        int numRetries = 1;
        int retryCount = 0;
        while ( session == null && retryCount <= numRetries ) {
            try {
                session = getSession();
            } catch (ConnectionFailedException e) {
                if ( retryCount++ < numRetries ) {
                    // force a reconnect
                    connection = null;
                } else {
                    throw e;
                }
            }
        }
        return session;
    }

    /**
     * Retrieve a session
     *
     * @return a new session
     * @throws JMSException
     */
    private Session getSession() throws JMSException {

        if ( connection == null ) {

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(getBrokerURL());

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
     * Get the corresponding queue (name) for this log level, or null.
     *
     * @param level log level
     * @return the queue name or null if the message should not be logged
     */
    private String getQueueName(LogLevelType level) {

        if ( level.compareTo(getConfigThreshold(getConfig().getErrorThreshold())) <= 0 ) {
            return getErrorQueueName();
        } else if ( level.compareTo(getConfigThreshold(getConfig().getInfoThreshold())) <= 0 ) {
            return getInfoQueueName();
        }
        return null;
    }

    private LogLevelType getConfigThreshold(String threshold) {
        try {
            return LogLevelType.valueOf(threshold);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            for ( LogLevelType value : LogLevelType.values() ) {
                sb.append(value).append(",");
            }
            String msg = "'" + threshold + "' is not a valid threshold log level. Must be one of " + sb.toString();
            System.err.println(msg);
            throw new IllegalArgumentException(msg);
        }
    }


}