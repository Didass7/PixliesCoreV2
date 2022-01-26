package net.pixlies.core.calendar;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MickMMars
 * This is the class + runner for the Pixlies in-game calendar.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PixliesCalendar extends Thread {

    private int day;
    private int month;
    private int year;
    private long prevTime;
    private long currTime;
    private boolean exit = false;
    private static final int daysInYear = 360;
    private static final int daysInMonth = 30;
    private static final int monthsInYear = 12;
    private static final Map<Integer, Runnable> events = new HashMap<>();

    public PixliesCalendar(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDayInYear() {
        return month * daysInMonth + day;
    }

    public boolean day() {
        return getWorld().getTime() < 12300 || getWorld().getTime() > 23850;
    }

    public World getWorld() {
        return Bukkit.getWorld("world");
    }

    /**
     * This code gets executed once a day is passed.
     *
     * It first increments the number of the days, if necessary, increments the number
     * of the month. If necessary it increments the number of the year.
     *
     * If it encounters an event from calendar.yml, it executes the event runner.
     */
    public void dayPassed() {
        this.day++;
        if (this.day > daysInMonth) {
            this.month++;
            this.day = 1;
        }
        if (this.month > monthsInYear) {
            this.year++;
            this.day = 1;
            this.month = 1;
        }
        if (events.containsKey(getDayInYear())) events.get(getDayInYear()).run();
        save();
    }

    public PixliesSeasons getSeason() {
        return switch (month) {
            case 4, 5, 6 -> PixliesSeasons.SUMMER;
            case 7, 8, 9 -> PixliesSeasons.AUTUMN;
            case 10, 11, 12 -> PixliesSeasons.WINTER;
            default -> PixliesSeasons.SPRING;
        };
    }

    public String formatDate() {
        return day + "/" + month + "/" + year;
    }

    public void startRunner() {
        prevTime = 0L;
        currTime = (getWorld().getTime() + 6000L) % 24000L;
        this.start();
    }

    public void run() {
        while (!exit) {
            this.prevTime = this.currTime;
            this.currTime = (getWorld().getTime() + 6000L) % 24000L;
            if (this.currTime < this.prevTime) dayPassed();
        }
    }

    public void stopRunner() {
        this.exit = true;
    }

    public void save() {
        Main instance = Main.getInstance();
        instance.getCalendarConfig().set("date", day + "/" + month + "/" + year);
        instance.getCalendarConfig().save();
    }

    static {
        Config calconf = Main.getInstance().getCalendarConfig();

        for (String s : calconf.getConfigurationSection("events").getKeys(false)) {
            events.put(Integer.parseInt(s), () -> {
                if (calconf.contains("events." + s + ".todo")) {
                    for (String todo : calconf.getStringList("events." + s + ".todo")) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), todo);
                    }
                }
            });
        }
    }

    public enum PixliesSeasons {

        SPRING("\uD83C\uDF33"),
        SUMMER("\uD83C\uDFD6"),
        AUTUMN("\uD83C\uDF42"),
        WINTER("☃");

        private @Getter final String icon;

        PixliesSeasons(String icon) {
            this.icon = icon;
        }

    }

}
