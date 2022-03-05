package net.pixlies.core.entity.user.timers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Timer class to do user timer things.
 * @author Dynmie
 */
@Data
@AllArgsConstructor
public class Timer {

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
        return startTime < getEndTime();
    }

    /**
     * Reset the timer.
     */
    public void reset() {
        long newStartTime = System.currentTimeMillis();
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
        return new SimpleDateFormat("hh:mm:ss").format(getEndTime());
    }

    /**
     * Get the time remaining in millis
     * Returns 0 if the timer has ended.
     * @return Time remaining in millis
     */
    public long getTimeRemaining() {
        if (isExpired()) return 0;
        return getEndTime() - startTime;
    }

}
