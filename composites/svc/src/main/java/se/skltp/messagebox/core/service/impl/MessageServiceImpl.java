package se.skltp.messagebox.core.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.skltp.messagebox.core.service.MessageService;
import se.skltp.messagebox.exception.ServiceContractTypeNotStorableException;

/**
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class) // make sure to rollback for all Exceptions
public class MessageServiceImpl implements MessageService {

    /**
     * The default answer given when a service contract is stored.
     *
     * May be overriden by giving a response is specified in the properties file.
     */
    private static final String DEFAULT_SERVICE_CONTRACT_OK_RESPONSE = "<response>Ok</response>";

    @Value("${max.fetch.results}")
    int maxResults;

    @Autowired
    private MessageRepository messageRepository;
    private Properties properties;

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

        if ( numDeletedMessages != ids.size() ) {
            throw new IllegalStateException("Unable to delete " + ids.size()
                    + " messages, succeeded with " + numDeletedMessages);
        }

    }


    @Override
    public String getOkResponseForServiceContract(String serviceContractType) throws ServiceContractTypeNotStorableException {
        if ( properties == null ) {
            properties = new Properties();
            try {
                properties.load(getClass().getResourceAsStream("/serviceContracts.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String result = properties.getProperty(serviceContractType);
        if (result == null) {
            throw new ServiceContractTypeNotStorableException();
        }
        if (result.trim().length() == 0) {
            result = DEFAULT_SERVICE_CONTRACT_OK_RESPONSE;
        }
        return result;
    }
}
