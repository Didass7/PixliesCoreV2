package net.pixlies.core;

import lombok.Getter;
import lombok.Setter;
import net.pixlies.core.calendar.PixliesCalendar;
import net.pixlies.core.commands.CommandManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.MongoDB;
import net.pixlies.core.handlers.HandlerManager;
import net.pixlies.core.handlers.RegisterHandlerManager;
import net.pixlies.core.modules.ModuleManager;
import net.pixlies.core.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    @Getter private static Main instance;

    @Getter private MongoDB database;
    @Getter private HandlerManager handlerManager;
    @Getter private ModuleManager moduleManager;
    @Getter private CommandManager commandManager;
    @Getter private PixliesCalendar calendar;

    @Getter private Config config;
    @Getter private Config calendarConfig;

    @Override
    public void onEnable() {

        instance = this;

        // HANDLERS
        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAllHandlers();

        // CONFIGURATION
        config = new Config(new File(getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");
        calendarConfig = new Config(new File(getDataFolder().getAbsolutePath() + "/calendar.yml"), "calendar.yml");

        // DATABASE
        database = new MongoDB().init();

        // PIXLIES CALENDAR
        String[] date = calendarConfig.getString("date", "0/0/0").split("/");
        calendar = new PixliesCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        calendar.startRunner();


        // LISTENERS & COMMANDS
        ListenerManager.registerAllListeners();
        commandManager = new CommandManager();
        commandManager.registerAllCommands();

        // MODULES
        moduleManager = new ModuleManager();
        moduleManager.loadModules();

    }

    @Override
    public void onDisable() {
        moduleManager.unloadModules();
    }

}
