package se.skltp.messagebox.core.service.impl;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.core.StatusReport;
import se.skltp.messagebox.core.entity.Message;
import se.skltp.messagebox.core.repository.MessageRepository;
import se.skltp.messagebox.core.service.MessageService;
import se.skltp.messagebox.core.service.StatisticService;
import se.skltp.messagebox.exception.InvalidServiceContractTypeException;

/**
 * Service implementation.
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class) // make sure to rollback for all Exceptions
public class MessageServiceImpl implements MessageService {

    /**
     * The default answer given when a service contract is stored.
     * <p/>
     * May be overriden by giving a response is specified in the properties file.
     */
    private static final String DEFAULT_SERVICE_CONTRACT_OK_RESPONSE = "<response>Ok</response>";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatisticService statisticService;

    private Properties properties;

    public List<Message> getMessages(String receiverId, Set<Long> ids) {
        List<Message> messages = messageRepository.getMessages(receiverId, ids);

        // mark the message as retrieved
        for ( Message msg : messages ) {
            msg.setStatusRetrieved();
        }

        return messages;

    }

    @Override
    public List<Message> listMessages(String receiverId) {
        return messageRepository.listMessages(receiverId);
    }

    public Long saveMessage(Message message) {
        Message result = messageRepository.store(message);
        return result.getId();
    }

    public void deleteMessages(String receiverId, long timestamp, List<Message> messages) {
        Set<Long> ids = new HashSet<>();
        for ( Message msg : messages ) {
            ids.add(msg.getId());
        }
        int numDeleted = messageRepository.delete(receiverId, ids);
        if ( numDeleted != messages.size() ) {
            throw new IllegalStateException("Unable to delete " + messages.size() + " ids, could only delete " + numDeleted + " ids!");
        }
        statisticService.addDeliveriesToStatistics(receiverId, timestamp, messages);
    }


    @Override
    public String getOkResponseForServiceContract(String serviceContractType) throws InvalidServiceContractTypeException {
        if ( properties == null ) {
            properties = new Properties();
            try {
                properties.load(getClass().getResourceAsStream("/serviceContracts.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String result = properties.getProperty(serviceContractType);
        if ( result == null ) {
            throw new InvalidServiceContractTypeException(serviceContractType);
        }
        if ( result.trim().length() == 0 ) {
            result = DEFAULT_SERVICE_CONTRACT_OK_RESPONSE;
        }
        return result;
    }


    @Override
    public List<StatusReport> getStatusReports() {
        return messageRepository.getStatusReports();
    }

}
