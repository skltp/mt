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

    private long timeDelta;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;

    public TimeDelta(long timeDelta) {
        this.timeDelta = timeDelta;
        long td = timeDelta;
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


    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ( days > 0 ) {
            sb.append(days).append("d ");
        }
        if ( days > 0 || hours > 0 ) {
            if (hours < 10) {
                sb.append("0");
            }
            sb.append(hours).append(":");
        }
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes).append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds).append(".");
        if (milliseconds < 10) {
            sb.append("0");
        }
        if (milliseconds < 100) {
            sb.append("0");
        }
        sb.append(milliseconds);

        return sb.toString();
    }

    public long getTimeDelta() {
        return timeDelta;
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
