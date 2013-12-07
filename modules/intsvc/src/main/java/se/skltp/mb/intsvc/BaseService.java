/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera MessageService (http://code.google.com/p/inera-message).
 *
 * Inera MessageService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera MessageService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.skltp.mb.intsvc;

import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import se.riv.infrastructure.itintegration.messagebox.v1.MessageStatusType;
import se.skltp.mb.svc.loghandler.ContextData;
import se.skltp.mb.svc.loghandler.JMSQueueAppender;
import se.skltp.mb.svc.services.MessageService;
import se.skltp.mb.types.entity.MessageMeta;
import se.skltp.mb.types.entity.MessageStatus;


/**
 * Base class for services
 *
 * @author mats.olsson@callistaenterprise.se
 */
public abstract class BaseService {
    // the name of the Http-Header for the callings system authentification id
    public static String SERVICE_CONSUMER_HSA_ID_HEADER_NAME = "x-rivta-original-serviceconsumer-hsaid";
    public static String MULÈ_CORRELATION_ID_HEADER_NAME = "x-mule_correlation_id";
    protected MessageService messageService;
    protected WebServiceContext wsContext;
    public static final String COMMON_TARGET_SYSTEM = "Common";


    @Resource
    public void setWsContext(WebServiceContext wsContext) {
        this.wsContext = wsContext;
    }

    @Resource
    public void setMessageService(MessageService MessageService) {
        this.messageService = MessageService;
    }


    /**
     * Extract the hsa-id for the caller from the http-header.
     * <p/>
     * The hsa-id for caller is placed by the VP in the http-header of the call.
     *
     * @return the hsa-id from the caller
     */
    protected String extractCallingSystemFromRequest() {
        return getHeaderValue(SERVICE_CONSUMER_HSA_ID_HEADER_NAME);
    }

    /**
     * Extract the correlation id from the the http-header.
     * <p/>
     * The correlation id is a tag attached to messages by the VP (mule).
     *
     * @return the correlation id in the current messageContext.
     */
    protected String extractCorrelationIdFromRequest() {
        return getHeaderValue(MULÈ_CORRELATION_ID_HEADER_NAME);
    }


    protected String extractTargetSystemFromUrl() {
        //
        // INFRA-51: We are unable at this time to actually have per-target-system changes
        // due to lack of time/communication with the customer. We patch this by using only one targetSystem which
        // we name "CommonSystem"
        //
        return COMMON_TARGET_SYSTEM;

        /*
        Extract target system from
        MessageContext ctx = wsContext.getMessageContext();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.get(MessageContext.SERVLET_REQUEST);
        String uri = servletRequest.getRequestURI();
        int n = uri.indexOf(ENDPOINT_NAME);
        if ( n == -1 ) {
            throw new RuntimeException("Unable to find my own endpoint name \"" + ENDPOINT_NAME + "\" in uri \"" + uri + "\"");
        }
        String encodedHsaId = uri.substring(n + ENDPOINT_NAME.length());
        return uf8DecodeUri(encodedHsaId);
        */
    }

    protected String extractTargetSystemFromRequest() {
        //
        // INFRA-51: See above,
        //
        return COMMON_TARGET_SYSTEM;

        // return actual target system
        // return extractCallingSystemFromRequest();
    }


    private String getHeaderValue(String name) {
        MessageContext msgCtxt = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) msgCtxt.get(MessageContext.SERVLET_REQUEST);
        return req.getHeader(name);
    }

    protected String describeMessageDiffs(Collection<Long> messageIds, List<MessageMeta> messages) {
        List<Long> sorted = getMissingMessageIds(messageIds, messages);

        StringBuilder result = new StringBuilder();

        for ( Long messageId : sorted ) {
            if ( result.length() > 0 ) {
                result.append(", ");
            }
            result.append(messageId);
        }
        return result.toString();
    }

    private List<Long> getMissingMessageIds(Collection<Long> messageIds, List<MessageMeta> messages) {
        Set<Long> copy = new HashSet<Long>(messageIds);
        for ( MessageMeta message : messages ) {
            copy.remove(message.getId());
        }
        List<Long> sorted = new ArrayList<Long>(copy);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Translates the entity MessageStatus to the schema MessageStatusType.
     * <p/>
     * Exist because we don't want the types modules to be dependent on the schema module
     *
     * @param status entity status to translate
     * @return translated into schema
     */
    public static MessageStatusType translateStatusToSchema(MessageStatus status) {
        switch (status) {
            case RECEIVED:
                return MessageStatusType.RECEIVED;
            case RETRIEVED:
                return MessageStatusType.RETRIEVED;
            case DELETED:
                return MessageStatusType.DELETED;
            default:
                throw new RuntimeException("Illegal message status " + status + " found!");
        }
    }


    /**
     * Log a custom info message
     *
     * @param logger
     * @param messageText the log message
     * @param messageId   the if of the message that should be logged
     * @param message
     */
    public void logInfo(Logger logger, String messageText, String messageId, MessageMeta message) {
        log(logger, Level.INFO, messageText, messageId, message, null);
    }

    /**
     * Log a custom warn message
     *
     * @param logger
     * @param messageText the log message
     * @param messageId   the if of the message that should be logged
     * @param message
     * @param e           exception that should be logged as a throwable
     */
    public void logWarn(Logger logger, String messageText, String messageId, MessageMeta message, Exception e) {
        log(logger, Level.WARN, messageText, messageId, message, e);
    }

    /**
     * Log a custom error message
     *
     * @param logger
     * @param messageText the log message
     * @param messageId   the if of the message that should be logged
     * @param message
     * @param e           exception that should be logged as a throwable
     */
    public void logError(Logger logger, String messageText, String messageId, MessageMeta message, Exception e) {
        log(logger, Level.ERROR, messageText, messageId, message, e);
    }



    /**
     * Log a custom  message
     *
     * @param logger
     * @param l           what log level that message should be logged as
     * @param messageText the log message
     * @param messageId   the if of the message that should be logged
     * @param message
     * @param e           exception that should be logged as a throwable
     */
    private void log(Logger logger, Level l, String messageText, String messageId, MessageMeta message, Exception e) {

        String originalCorrelationId = message != null ? message.getCorrelationId() : null;
        ContextData data = new ContextData(extractCorrelationIdFromRequest(), originalCorrelationId, messageId);
        JMSQueueAppender.setContextData(data);

        if ( logger != null ) {

            switch (l.toInt()) {

                case Level.INFO_INT:
                    logger.info(messageText);
                    break;

                case Level.WARN_INT:
                    logger.warn(messageText, e);
                    break;

                case Level.ERROR_INT:
                    logger.error(messageText, e);

                default:
                    break;
            }
        } else {
            System.err.println("Null logger when trying to log " + messageText);
        }
    }


    /**
     * All subclasses needs to provide this method for allow access to their logger
     *
     * @return Logger
     */
    public abstract Logger getLogger();


    /**
     * Reset the "data channel" so that we don't pollute the thread.
     */
    public void resetLogContext() {
        JMSQueueAppender.setContextData(null);
    }
}