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
package se.skltp.messagebox.services;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.springframework.web.util.UriUtils;
import se.riv.itintegration.messagebox.v1.MessageStatusType;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.entity.MessageStatus;
import se.skltp.messagebox.core.service.MessageService;

/**
 * Base class for services
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class BaseService {
    // the name of the Http-Header for the callings system authentification id
    public static String SERVICE_CONSUMER_HSA_ID_HEADER_NAME = "x-rivta-original-serviceconsumer-hsaid";
    protected MessageService messageService;
    protected WebServiceContext wsContext;



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
    protected String extractCallerIdFromRequest() {
        return getHeaderValue(SERVICE_CONSUMER_HSA_ID_HEADER_NAME);
    }


    private String getHeaderValue(String name) {
        MessageContext msgCtxt = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) msgCtxt.get(MessageContext.SERVLET_REQUEST);
        return req.getHeader(name);
    }

    protected String describeMessageDiffs(Set<Long> messageIds, List<Message> messages) {
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

    private List<Long> getMissingMessageIds(Set<Long> messageIds, List<Message> messages) {
        Set<Long> copy = new HashSet<Long>(messageIds);
        for ( Message message : messages ) {
            copy.remove(message.getId());
        }
        List<Long> sorted = new ArrayList<Long>(copy);
        Collections.sort(sorted);
        return sorted;
    }

    protected String uf8DecodeUri(String encodedHsaId) {
        try {
            return UriUtils.decode(encodedHsaId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String utf8EncodeUriFragment(String consumerHsaId) {
        try {
            return UriUtils.encodeFragment(consumerHsaId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);   // not reachable, tied to "utf-8"
        }
    }

    /**
     * Translates the entity MessageStatus to the schema MessageStatusType.
     *
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
}
