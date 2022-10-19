package net.pixlies.core.utils;

public class TimeUnit {

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long WEEK = DAY * 7;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = DAY * 365;

    private TimeUnit() {}

    /**
     * Get a long duration from a string.
     * @param duration the string duration
     * @return -1 if duration is not a valid duration.
     */
    public static long getDuration(String duration) {
        long multiplier;
        try {
            multiplier = Integer.parseInt(duration.substring(0, duration.length() - 1));
        } catch (NumberFormatException e) {
            return -1;
        }
        return switch (duration.substring(duration.length() - 1)) {
            case "s" -> SECOND * multiplier;
            case "m" -> MINUTE * multiplier;
            case "h" -> HOUR * multiplier;
            case "d" -> DAY * multiplier;
            case "w" -> WEEK * multiplier;
            case "M" -> MONTH * multiplier;
            case "y" -> YEAR * multiplier;
            default -> multiplier;
        };
    }

}
