package se.skltp.messagebox.svc.loghandler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.soitoolkit.commons.logentry.schema.v1.*;

public class LogEventCreator {

	
	/**
	 * Create a new instance of LogEvent based on a LoggingEvent.
	 * 
	 * @param event
	 * @param contextData class containing meta data. (Optional)
	 * @return a instance of LogEvent assembled of data from the LoggingEvent
	 * @throws Exception
	 */
	public static LogEvent createLogEvent(LoggingEvent event, ContextData contextData, String componentName) throws Exception {
		
		LogEvent logEvent = initlizeLogEventStructure();
		
		// Add the log message
		String msg = event.getMessage().toString();
		logEvent.getLogEntry().getMessageInfo().setMessage(msg);

		// Add exception related information
		if (containsThrowable(event)) {
			logEvent.getLogEntry().getMessageInfo().setException(assembleLogMessageException(event));
		}
		
		// Add runtime related information
		logEvent.getLogEntry().setRuntimeInfo(assembleLogRuntimeInfo(event, contextData, componentName));

        logEvent.getLogEntry().getExtraInfo().addAll(assembleLogExtraInfo(event, contextData));

		// Add logLevel
		logEvent.getLogEntry().getMessageInfo().setLevel(assembleLogLevel(event));
		
		return logEvent;
	}

    private static Collection<? extends LogEntryType.ExtraInfo> assembleLogExtraInfo(LoggingEvent event, ContextData contextData) {
        Collection<LogEntryType.ExtraInfo> results = new ArrayList<LogEntryType.ExtraInfo>();

        if (contextData.getOriginalCorrelationId() != null) {
            LogEntryType.ExtraInfo info = new LogEntryType.ExtraInfo();
            info.setName("original-businessCorrelationId");
            info.setValue(contextData.getOriginalCorrelationId());
            results.add(info);
        }

        return results;
    }


    private static LogEvent initlizeLogEventStructure() {
		LogEvent logEvent  = new LogEvent();
		LogEntryType logEntry = new LogEntryType();

		logEvent.setLogEntry(logEntry);
		logEntry.setMessageInfo(new LogMessageType());
		
		return logEvent;
	}
	
	
	private static LogMessageExceptionType assembleLogMessageException(LoggingEvent event) {
	
		LogMessageExceptionType exceptionMessage = new LogMessageExceptionType();
		exceptionMessage.setExceptionClass(event.getThrowableInformation().getThrowable().getClass().toString());
		
		String trace = "";
		for(StackTraceElement el : event.getThrowableInformation().getThrowable().getStackTrace()) {
			trace = trace + el.toString() + "\n";
		}
		exceptionMessage.setExceptionMessage(trace);
		
		return exceptionMessage;
	}
	
	
	private static LogRuntimeInfoType assembleLogRuntimeInfo(LoggingEvent event, ContextData contextData, String componentName) throws UnknownHostException, DatatypeConfigurationException {

		LogRuntimeInfoType runtimeMessage = new LogRuntimeInfoType();
		
		// Add data from the context
		if (contextData != null) {
			if (contextData.getCorrelationId() != null) runtimeMessage.setBusinessCorrelationId(contextData.getCorrelationId());
			if (contextData.getMessageId() != null) runtimeMessage.setMessageId(contextData.getMessageId());
		}
		
		// Add the remaining information
		runtimeMessage.setComponentId(componentName);
		runtimeMessage.setHostName(InetAddress.getLocalHost().getHostName());
		runtimeMessage.setTimestamp(convertTimestampToGregorialCalendar(event));

		return runtimeMessage;
	}
	
	
	private static XMLGregorianCalendar convertTimestampToGregorialCalendar(LoggingEvent event) throws DatatypeConfigurationException {
		GregorianCalendar timestamp = new GregorianCalendar();
		timestamp.setTimeInMillis(event.timeStamp);
		
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(timestamp);
	}
	
	/**
	 * Translates between LoggingEvent level to LogLevelType
	 * 
	 * @param event
	 * @return
	 * @throws Exception when there is a unknown log level.
	 */
	private static LogLevelType assembleLogLevel(LoggingEvent event) throws Exception {
			
		Level l = event.getLevel();

		if (l.equals(Level.INFO)) {
			return LogLevelType.INFO;

		} else if (l.equals(Level.WARN)) {
			return LogLevelType.WARNING;

		} else if (l.equals(Level.ERROR)) {
			return LogLevelType.ERROR;

		} else if (l.equals(Level.DEBUG)) {
			return LogLevelType.DEBUG;
		}

		throw new Exception("Invalid loglevel: " + l.toString());
	}
	
	
	private static boolean containsThrowable(LoggingEvent event) {
		return event.getThrowableInformation() != null;
	}
	
}
