package se.skltp.messagebox.core.service;

import se.skltp.messagebox.core.entity.SystemProperty;

/**
 * @author mats.olsson@callistaenterprise.se
 */
public interface SystemPropertyService {

    SystemProperty getProperty(String name, String defaultValue);

    SystemProperty getReceiveMessageUrl();
}
