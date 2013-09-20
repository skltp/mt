package se.skltp.messagebox.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.skltp.messagebox.core.entity.SystemProperty;
import se.skltp.messagebox.core.repository.SystemPropertyRepository;
import se.skltp.messagebox.core.service.SystemPropertyService;

/**
 *
 * @author mats.olsson@callistaenterprise.se
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemPropertyServiceImpl implements SystemPropertyService {


    @Autowired
    private SystemPropertyRepository repository;

    @Override
    public SystemProperty getProperty(String name, String defaultValue) {
        return repository.getProperty(name, defaultValue);
    }

    @Override
    public SystemProperty getReceiveMessageUrl() {
        return getProperty("RECEIVE_MESSAGE_URL", "localhost:8081/ReceiveMessage");
    }
}
