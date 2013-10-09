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
package se.skltp.messagebox;

/**
 * Describes a time delta.
 *
 * @author mats.olsson@callistaenterprise.se
 */
public class TimeDelta {
    public static final long MS_SECOND = 1000;
    public static final long MS_MINUTE = 60 * MS_SECOND;
    public static final long MS_HOUR = 60 * MS_MINUTE;
    public static final long MS_DAY = 24 * MS_HOUR;

    private long deltaTime;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;
    private Precision precision;

    public static enum Precision {
        ms,
        sec,
        min,
        hour
    }


    /**
     * Create a time delta and display it in the given precision
     *
     * @param deltaTime time delta in milliseconds
     * @param precision precision to use when displaying it
     */
    public TimeDelta(long deltaTime, Precision precision) {
        this.deltaTime = deltaTime;
        this.precision = precision;
        long td = deltaTime;
        days = (int) (td / MS_DAY);
        td -= days * MS_DAY;
        hours = (int) (td / MS_HOUR);
        td -= hours * MS_HOUR;
        minutes = (int) (td / MS_MINUTE);
        td -= minutes * MS_MINUTE;
        seconds = (int) (td / MS_SECOND);
        td -= seconds * MS_SECOND;
        milliseconds = (int) td;
    }

    /**
     * Create a time delta and display it in default (sec) precision.
     *
     * @param deltaTime in milliseconds
     */
    public TimeDelta(long deltaTime) {
        this(deltaTime, Precision.sec);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ( days > 0 ) {
            sb.append(days).append("d ");
        }
        if ( days > 0 || hours > 0 ) {
            sb.append(pad(hours, "00"));
        }
        if ( precision.ordinal() <= Precision.min.ordinal() ) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(pad(minutes, "00"));
            if ( precision.ordinal() <= Precision.sec.ordinal() ) {
                sb.append(":").append(pad(seconds, "00"));
                if ( precision == Precision.ms ) {
                    sb.append(".").append(pad(milliseconds, "000"));
                }
            }
        }
        return sb.toString();
    }

    private String pad(long v, String pad) {
        String s = pad + v;
        return s.substring(s.length() - pad.length());
    }

    public long getDeltaTime() {
        return deltaTime;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }
}
