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
package se.skltp.mb.types.services;

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
     * As {@link #now()}, but returns a date for the current time.
     *
     * @return now as a date
     */
    Date date();
}
