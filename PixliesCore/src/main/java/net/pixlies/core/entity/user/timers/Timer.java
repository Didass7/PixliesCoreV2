package net.pixlies.core.entity.user.timers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.UUID;

/**
 * Timer class to do user timer things.
 * @author Dynmie
 */
@Data
public class Timer { // FIXME: HIDDEN TIMERS, SELF RUN

    private final PeriodFormatter aboveMinuteFormatter = new PeriodFormatterBuilder()
            .minimumPrintedDigits(2)
            .printZeroRarelyFirst()
            .appendDays()
            .appendSeparator(":")
            .appendHours()
            .appendSeparator(":")
            .printZeroAlways()
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();

    private final PeriodFormatter belowMinuteFormatter = new PeriodFormatterBuilder()
            .minimumPrintedDigits(1)
            .printZeroAlways()
            .appendSeconds()
            .appendSeparator(".")
            .appendMillis3Digit()
            .toFormatter();

    private final UUID identifier = UUID.randomUUID();
    private ChatColor color;
    private String displayName;
    private long startTime;
    private long duration;
    private boolean hidden;

    private @Getter(AccessLevel.NONE) boolean forceExpired = false;

    public Timer(ChatColor color, String displayName, long startTime, long duration, boolean hidden) {
        this.color = color;
        this.displayName = displayName;
        this.startTime = startTime;
        this.duration = duration;
        this.hidden = hidden;
    }

    /**
     * Check if the timer has finished.
     * @return True if completed; False if ongoing.
     */
    public boolean isForceExpired() {
        if (forceExpired) return true;
        return System.currentTimeMillis() > getEndTime();
    }

    public void expire() {
        forceExpired = true;
    }

    /**
     * Reset the timer.
     */
    public void reset() {
        forceExpired = false;
        startTime = System.currentTimeMillis();
    }

    /**
     * Get the end time in millis.
     * @return End time in millis
     */
    public long getEndTime() {
        return startTime + duration;
    }

    /**
     * Get the duration formatted as HH:MM:SS
     * @return Formatted duration
     */
    public String getRemainingFormatted() {
        long remaining = getTimeRemaining();
        Duration duration = new Duration(remaining);
        if (remaining < 60000) { // 1 minute
            String formatted = belowMinuteFormatter.print(duration.toPeriod().normalizedStandard()); // mm:ss.SS
            return formatted.substring(0, formatted.length() - 2);
        }
        return aboveMinuteFormatter.print(duration.toPeriod().normalizedStandard()); // dd:hh:mm:ss
    }

    /**
     * Get the time remaining in millis
     * Returns 0 if the timer has ended.
     * @return Time remaining in millis
     */
    public long getTimeRemaining() {
        if (isForceExpired()) return 0;
        return getEndTime() - System.currentTimeMillis();
    }

}
