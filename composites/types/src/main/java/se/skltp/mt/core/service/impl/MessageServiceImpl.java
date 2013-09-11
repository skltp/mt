package se.skltp.mt.core.service.impl;

import java.util.List;
import java.util.Set;

import se.skltp.mt.core.entity.Message;
import se.skltp.mt.core.service.MessageService;

/**
 * Created by matsolsson on 9/10/13 11:32
 */
public class MessageServiceImpl implements MessageService {
    public List<Message> getAllMessagesForSystem(String systemId) {
        throw new RuntimeException("NYI"); // TODO: Implement!
    }

    public Long saveMessage(Message Message) {
        throw new RuntimeException("NYI"); // TODO: Implement!
    }

    public void deleteMessagesForCareUnit(String careUnit, Set<Long> ids) {
        throw new RuntimeException("NYI"); // TODO: Implement!
    }
}
