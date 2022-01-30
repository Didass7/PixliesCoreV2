package net.pixlies.core;

import lombok.Getter;
import net.pixlies.core.calendar.PixliesCalendar;
import net.pixlies.core.commands.CommandManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.MongoManager;
import net.pixlies.core.handlers.HandlerManager;
import net.pixlies.core.handlers.RegisterHandlerManager;
import net.pixlies.core.listeners.ListenerManager;
import net.pixlies.core.modules.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    @Getter private static Main instance;

    @Getter private MongoManager database;
    @Getter private HandlerManager handlerManager;
    @Getter private ModuleManager moduleManager;
    @Getter private CommandManager commandManager;
    @Getter private PixliesCalendar calendar;

    @Getter private Config config;
    @Getter private Config calendarConfig;
    @Getter private Config settings;

    @Override
    public void onEnable() {
        instance = this;

        // CONFIGURATION
        config = new Config(new File(getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");
        settings = new Config(new File(getDataFolder().getAbsolutePath() + "/settings.yml", "settings.yml"));
        calendarConfig = new Config(new File(getDataFolder().getAbsolutePath() + "/calendar.yml"), "calendar.yml");

        // HANDLERS
        moduleManager = new ModuleManager();
        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAllHandlers();

        // DATABASE
        database = new MongoManager().init();

        // PIXLIES CALENDAR
        String[] date = calendarConfig.getString("date", "0/0/0").split("/");
        calendar = new PixliesCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        calendar.startRunner();

        // LISTENERS & COMMANDS
        ListenerManager.registerAllListeners();
        commandManager = new CommandManager();

        // MODULES
        moduleManager.loadModules();

    }

    @Override
    public void onDisable() {
        moduleManager.unloadModules();
        instance = null;
    }

}
