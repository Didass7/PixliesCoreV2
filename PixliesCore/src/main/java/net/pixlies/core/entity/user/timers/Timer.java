package net.pixlies.core.entity.user.timers;

import lombok.AllArgsConstructor;
import lombok.Data;
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
@AllArgsConstructor
public class Timer {

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
            .minimumPrintedDigits(2)
            .printZeroAlways()
            .appendSeconds()
            .appendSeparator(".")
            .appendMillis3Digit()
            .toFormatter();

    private final UUID uniqueId = UUID.randomUUID();
    private ChatColor color;
    private String displayName;
    private long startTime;
    private final long duration;

    /**
     * Check if the timer has finished.
     * @return True if completed; False if ongoing.
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > getEndTime();
    }

    /**
     * Reset the timer.
     */
    public void reset() {
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
        if (remaining < 60000) { // 1 hour
            String formatted = belowMinuteFormatter.print(duration.toPeriod().normalizedStandard()); // mm:ss.SS
            return formatted.substring(0, formatted.length() - 1);
        }
        return aboveMinuteFormatter.print(duration.toPeriod().normalizedStandard()); // dd:hh:mm:ss
    }

    /**
     * Get the time remaining in millis
     * Returns 0 if the timer has ended.
     * @return Time remaining in millis
     */
    public long getTimeRemaining() {
        if (isExpired()) return 0;
        return getEndTime() - System.currentTimeMillis();
    }

}
