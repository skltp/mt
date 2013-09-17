package se.skltp.messagebox.core.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.skltp.messagebox.core.service.MessageService;

/**
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class) // make sure to rollback for all Exceptions
public class MessageServiceImpl implements MessageService {

    @Value("${max.fetch.results}")
    int maxResults;

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesForSystem(String systemId, Set<Long> ids) {
        return messageRepository.getMessagesForSystem(systemId, ids);
    }

    @Override
    public List<Message> getAllMessagesForSystem(String systemId) {
        return messageRepository.getAllMessagesForSystem(systemId);
    }

    public Long saveMessage(Message message) {
        Message result = messageRepository.persist(message);
        return result.getId();
    }

    public void deleteMessagesForSystem(String systemId, Set<Long> ids) {
        int numDeletedMessages = messageRepository.delete(systemId, ids);

        // TODO: How to handle partial/failed deletes?
        // how is it supposed to be fixed? What problem can it be? Can we hide the
        // problem and fix it manually? Who is responsible and should get the error message
        //
        // potential problem is deleting non-read messages?

        if (numDeletedMessages != ids.size()) {
            throw new IllegalStateException("Unable to delete " + ids.size()
                    + " messages, succeeded with " + numDeletedMessages);
        }

    }
}
