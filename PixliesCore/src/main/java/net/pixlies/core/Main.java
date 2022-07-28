package net.pixlies.core;

import lombok.Getter;
import net.pixlies.core.calendar.PixliesCalendar;
import net.pixlies.core.commands.PixliesCommandManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.MongoManager;
import net.pixlies.core.handlers.HandlerManager;
import net.pixlies.core.handlers.RegisterHandlerManager;
import net.pixlies.core.handlers.impl.ScoreboardHandler;
import net.pixlies.core.listeners.ListenerManager;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.pluginmessaging.PixliesPluginMessageManager;
import net.pixlies.core.pluginmessaging.PluginMessageRegisterManager;
import net.pixlies.core.runnables.RunnableManager;
import net.pixlies.core.runnables.RunnableRegisterManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.pixlies.core.utils.FileUtils.saveIfNotExists;

public class Main extends JavaPlugin {

    @Getter private static Main instance;

    @Getter private MongoManager database;
    @Getter private HandlerManager handlerManager;
    @Getter private PixliesPluginMessageManager pluginMessageManager;
    @Getter private PluginMessageRegisterManager pluginMessageRegisterManager;
    @Getter private PixliesCommandManager commandManager;
    @Getter private RunnableManager runnableManager;
    @Getter private PixliesCalendar calendar;

    @Getter private Config config;
    @Getter private Config staffModeConfig;
    @Getter private Config calendarConfig;
    @Getter private Config settings;
    @Getter private Config warpsConfig;

    @Getter private boolean lobbyServer;

    private RunnableRegisterManager runnableRegisterManager;

    @Override
    public void onEnable() {
        instance = this;

        // CONFIGURATION
        config =            new Config(new File(getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");
        staffModeConfig =   new Config(new File(getDataFolder().getAbsolutePath() + "/staffmode/persist.yml"), "staffmode/persist.yml");
        settings =          new Config(new File(getDataFolder().getAbsolutePath() + "/settings.yml", "settings.yml"));
        calendarConfig =    new Config(new File(getDataFolder().getAbsolutePath() + "/calendar.yml"), "calendar.yml");
        warpsConfig =       new Config(new File(getDataFolder().getAbsolutePath() + "/warps.yml", "warps.yml"));

        // CHECK LOBBY
        lobbyServer = config.getBoolean("commands.limitedCommands", false);

        // CONFIG INIT
        saveIfNotExists("staffmode/persist.yml");

        // LANGUAGE
        saveIfNotExists("languages/LANG_GER.yml");
        saveIfNotExists("languages/LANG_ENG.yml");
        saveIfNotExists("languages/LANG_FRA.yml");
        saveIfNotExists("languages/LANG_ARM.yml");
        saveIfNotExists("languages/LANG_LUX.yml");
        saveIfNotExists("languages/LANG_PER.yml");
        Lang.init();

        // HANDLERS
        handlerManager = new HandlerManager();
        runnableManager = new RunnableManager();
        pluginMessageManager = new PixliesPluginMessageManager();
        pluginMessageRegisterManager = new PluginMessageRegisterManager();
        new RegisterHandlerManager().registerAllHandlers();

        // DATABASE
        database = new MongoManager().init();

        // PIXLIES CALENDAR
        String[] date = calendarConfig.getString("date", "0/0/0").split("/");
        calendar = new PixliesCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        calendar.startRunner();

        // LISTENERS & COMMANDS
        ListenerManager.registerAllListeners();
        commandManager = new PixliesCommandManager();

        // MESSAGING
        pluginMessageRegisterManager.registerAll();

        // RUNNABLES
        runnableRegisterManager = new RunnableRegisterManager();
        runnableRegisterManager.runAll();

        // SCOREBOARD
//      handlerManager.getHandler(ScoreboardHandler.class).load();

    }

    @Override
    public void onDisable() {
        runnableRegisterManager.stopAll();
        pluginMessageRegisterManager.unregisterAll();
        calendar.stopRunner();
        handlerManager.getHandler(ScoreboardHandler.class).unload();
        instance = null;
    }

}
