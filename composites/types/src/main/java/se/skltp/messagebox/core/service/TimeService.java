package se.skltp.messagebox.core.service;

import java.util.Date;

/**
 * Allows us to control the time used in the application so we can have consistent test cases.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public interface TimeService {
    /**
     * Return the current time in ms, same as System.currentTimeMillis()
     *
     * @return time in ms since 1970-01-01 00:00:00
     */
    long now();

    /**
     * As now, but returns a date for the current time.
     *
     * @return now as a date
     */
    Date date();
}
