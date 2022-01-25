package net.pixlies.core;

import lombok.Getter;
import net.pixlies.core.calendar.PixliesCalendar;
import net.pixlies.core.commands.CommandManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.MongoDB;
import net.pixlies.core.modules.ModuleManager;
import net.pixlies.core.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private static @Getter Main instance;

    @Getter private MongoDB database;
    @Getter private ModuleManager moduleManager;
    @Getter private CommandManager commandManager;
    @Getter private PixliesCalendar calendar;

    @Getter private Config calendarConfig;

    @Override
    public void onEnable() {
        instance = this;

        calendarConfig = new Config(new File(getDataFolder().getAbsolutePath() + "/calendar.yml"), "calendar.yml");

        String[] date = calendarConfig.getString("date", "0/0/0").split("/");
        calendar = new PixliesCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        calendar.startRunner();

        database = new MongoDB().init();
        moduleManager = new ModuleManager();
        moduleManager.loadModules();

        ListenerManager.registerAllListeners();
        commandManager = new CommandManager();
        commandManager.registerAllCommands();
    }

    @Override
    public void onDisable() {
        moduleManager.unloadModules();
    }

}
