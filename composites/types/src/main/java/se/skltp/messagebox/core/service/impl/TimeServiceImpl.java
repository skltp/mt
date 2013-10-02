package se.skltp.messagebox.core.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import se.skltp.messagebox.core.service.TimeService;

/**
 * @author mats.olsson@callistaenterprise.se
 */
@Service
public class TimeServiceImpl implements TimeService {
    @Override
    public long now() {
        return System.currentTimeMillis();
    }

    @Override
    public Date date() {
        return new Date(now());
    }
}
