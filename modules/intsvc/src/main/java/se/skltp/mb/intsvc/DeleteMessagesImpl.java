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

import java.util.List;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesResponseType;
import se.riv.infrastructure.itintegration.messagebox.DeleteMessagesResponder.v1.DeleteMessagesType;
import se.riv.infrastructure.itintegration.messagebox.v1.ResultCodeEnum;
import se.riv.infrastructure.itintegration.messagebox.v1.ResultType;
import se.skltp.mb.svc.exception.UnreadDeleteException;
import se.skltp.mb.types.entity.MessageMeta;
import se.skltp.mb.types.services.TimeService;

/**
 * Deletes the requested messages.
 * <p/>
 * Only read (ie fetched via GetMessages) messages can be deleted.
 * <p/>
 * Returns
 * <ul>
 * <li>{@link ResultCodeEnum#ERROR}, errorId = 1 on an {@link ErrorCodes#INTERNAL}</li>
 * <li>{@link ResultCodeEnum#ERROR}, errorId = 2 on an {@link ErrorCodes#UNREAD_DELETE} error</li>
 * <li>{@link ResultCodeEnum#OK} if all messages are deleted</li>
 * <li>{@link ResultCodeEnum#INFO} if not all messages are found (ie, have already been deleted)</li>
 * </ul>
 * <p/>
 */
@WebService(serviceName = "DeleteMessagesResponderService",
        endpointInterface = "se.riv.infrastructure.itintegration.messagebox.DeleteMessages.v1.DeleteMessagesResponderInterface",
        portName = "DeleteMessagesResponderPort",
        targetNamespace = "urn:riv:infrastructure:itintegration:messagebox:DeleteMessages:1:rivtabp21",
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
            List<MessageMeta> messages = messageService.listMessages(targetSystem, parameters.getMessageIds());
            if ( parameters.getMessageIds().size() != messages.size() ) {
                if ( log.isWarnEnabled() ) {
                    String msg = "Caller " + callingSystem + "  attempted to delete non-deletable messages " + describeMessageDiffs(parameters.getMessageIds(), messages);
                    logWarn(getLogger(), msg, null, null, null);
                }

                response.getResult().setCode(ResultCodeEnum.INFO);
                response.getResult().setErrorMessage(INCOMPLETE_ERROR_MESSAGE);
            }
            // none unread, delete them all!
            messageService.deleteMessages(targetSystem, timeService.now(), messages);

            List<Long> responseIds = response.getDeletedIds();
            for ( MessageMeta msg : messages ) {
                responseIds.add(msg.getId());
                if ( log.isInfoEnabled() ) {
                    String msgId = String.valueOf(msg.getId());
                    logInfo(getLogger(), "Message " + msgId + " was deleted by " + callingSystem, msgId, msg);
                }
            }

        } catch (UnreadDeleteException e) {
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorCodes.UNREAD_DELETE.ordinal());
            response.getResult().setErrorMessage(ErrorCodes.UNREAD_DELETE.getText() + " : " + e.getUnreadIdsAsCsv());

            if ( log.isWarnEnabled() ) {
                // Log at warning level that the user tried to do something silly
                for ( MessageMeta msg : e.getUnreadMessages() ) {
                    String msgId = String.valueOf(msg.getId());
                    logWarn(getLogger(), callingSystem + " attempted to delete unread message " + msgId, msgId, msg, null);
                }
            }
        } catch (Exception e) {

            if ( log.isErrorEnabled() ) {
                String msg = "Exception for ServiceConsumer " + callingSystem + " when trying to delete messages";
                logError(getLogger(), msg, null, null, e);
            }
            response.getResult().setCode(ResultCodeEnum.ERROR);
            response.getResult().setErrorId(ErrorCodes.INTERNAL.ordinal());
            response.getResult().setErrorMessage(ErrorCodes.INTERNAL.toString());
        } finally {
            resetLogContext();
        }
        return response;
    }


    @Override
    public Logger getLogger() {
        return log;
    }

}
