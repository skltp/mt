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
package se.skltp.messagebox.intsvc;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.itintegration.messagebox.v1.ResultType;
import se.skltp.messagebox.types.entity.MessageMeta;
import se.skltp.messagebox.types.entity.MessageStatus;
import se.skltp.messagebox.types.services.TimeService;

@WebService(serviceName = "DeleteMessagesResponderService",
        endpointInterface = "se.riv.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface",
        portName = "DeleteMessagesResponderPort",
        targetNamespace = "urn:riv:itintegration:messagebox:DeleteMessages:1:rivtabp21",
        wsdlLocation = "schemas/interactions/DeleteMessagesInteraction/DeleteMessagesInteraction_1.0_rivtabp21.wsdl")
public class DeleteMessagesImpl extends BaseService implements DeleteMessagesResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(DeleteMessagesImpl.class);

    private TimeService timeService;
    public static final String INCOMPLETE_ERROR_MESSAGE = "Incomplete delete";

    @Autowired
    public void setTimeService(TimeService service) {
        this.timeService = service;
    }


    @Override
    public DeleteMessagesResponseType deleteMessages(String logicalAddress, DeleteMessagesType parameters) {
        DeleteMessagesResponseType response = new DeleteMessagesResponseType();
        response.setResult(new ResultType());
        response.getResult().setCode(ResultCodeEnum.OK);

        String targetSystem = extractTargetSystemFromRequest();
        String callingSystem = extractCallingSystemFromRequest();

        try {
            // TODO: How to handle attempting to delete messages that have not been read yet?
            // how do we signal that error to the user? It is not incomplete in the ordinary sense - this
            // is a bug in the calling code system and must be signaled as a hard error..

            List<MessageMeta> messages = messageService.listMessages(targetSystem, parameters.getMessageIds());
            if ( parameters.getMessageIds().size() != messages.size() ) {

                String msg = "Caller " + callingSystem + "  attempted to delete non-deletable messages " + describeMessageDiffs(parameters.getMessageIds(), messages);
                logWarn(getLogger(), msg, null, null, null);


                // TODO: should we add an "infoId", "infoMessage"? to the result type? Or must reuse errorXxx?
                response.getResult().setCode(ResultCodeEnum.INFO);
                response.getResult().setErrorMessage(INCOMPLETE_ERROR_MESSAGE);
            }

            String unreadMessageCsv = checkUnreadMessages(messages);

            if ( unreadMessageCsv == null ) {
                // none unread, delete them all!
                messageService.deleteMessages(targetSystem, timeService.now(), messages);

                List<Long> responseIds = response.getDeletedIds();
                for ( MessageMeta msg : messages ) {
                    responseIds.add(msg.getId());
                }

                // Log deleted messages
                for ( MessageMeta msg : messages ) {
                    String msgId = String.valueOf(msg.getId());
                    logInfo(getLogger(), "Message " + msgId + " was deleted by " + callingSystem, msgId, msg);
                }

            } else {
                response.getResult().setCode(ResultCodeEnum.ERROR);
                response.getResult().setErrorId(ErrorCode.UNREAD_DELETE.ordinal());
                response.getResult().setErrorMessage(ErrorCode.UNREAD_DELETE.getText() + " : " + unreadMessageCsv);

                // Log at warning level that someone tried to do something silly
                for ( MessageMeta msg : messages ) {
                    String msgId = String.valueOf(msg.getId());
                    logWarn(getLogger(), callingSystem + " attempted to delete unread message " + msgId, msgId, msg, null);
                }
            }


        } catch (Exception e) {

            String msg = "Exception for ServiceConsumer " + callingSystem + " when trying to delete messages";
            logWarn(getLogger(), msg, null, null, e);

            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorCode.INTERNAL.ordinal());
            response.getResult().setErrorMessage(ErrorCode.INTERNAL.toString());
        } finally {
            resetLogContext();
        }
        return response;
    }

    /**
     * Returns a comma-separated-values list of unread message ids in the messages list, or null if none are unread.
     *
     * @param messages to check for status
     * @return null or a csv-list of message ids
     */
    private String checkUnreadMessages(List<MessageMeta> messages) {
        String result = null;
        // make sure all the messages have been read
        List<MessageMeta> unread = new ArrayList<MessageMeta>();
        for ( MessageMeta message : messages ) {
            if ( message.getStatus() != MessageStatus.RETRIEVED ) {
                unread.add(message);
            }
        }
        if ( !unread.isEmpty() ) {
            StringBuilder sb = new StringBuilder();
            for ( MessageMeta msg : unread ) {
                if ( sb.length() > 0 ) {
                    sb.append(",");
                }
                sb.append(msg.getId());
            }
            result = sb.toString();

        }
        return result;
    }

    @Override
    public Logger getLogger() {
        return log;
    }

}
