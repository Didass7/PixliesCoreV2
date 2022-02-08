package net.pixlies.core;

import lombok.Getter;
import net.pixlies.core.calendar.PixliesCalendar;
import net.pixlies.core.commands.CommandManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.MongoManager;
import net.pixlies.core.handlers.HandlerManager;
import net.pixlies.core.handlers.RegisterHandlerManager;
import net.pixlies.core.handlers.impl.ScoreboardHandler;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.Assemble;
import net.pixlies.core.lib.io.github.thatkawaiisam.assemble.AssembleStyle;
import net.pixlies.core.listeners.ListenerManager;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.modules.ModuleManager;
import net.pixlies.core.runnables.RunnableManager;
import net.pixlies.core.runnables.RunnableRegisterManager;
import net.pixlies.core.scoreboard.ScoreboardAdapter;
import net.pixlies.core.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;

import static net.pixlies.core.utils.FileUtils.saveIfNotExists;

public class Main extends JavaPlugin {

    @Getter private static Main instance;

    @Getter private MongoManager database;
    @Getter private HandlerManager handlerManager;
    @Getter private ModuleManager moduleManager;
    @Getter private CommandManager commandManager;
    @Getter private RunnableManager runnableManager;
    @Getter private PixliesCalendar calendar;

    @Getter private Config config;
    @Getter private Config staffModeConfig;
    @Getter private Config calendarConfig;
    @Getter private Config settings;

    @Getter private boolean lobbyServer;

    @Getter private Assemble assemble = null;
    @Getter private Scoreboard emptyScoreboard;

    private RunnableRegisterManager runnableRegisterManager;

    @Override
    public void onEnable() {
        instance = this;

        // CONFIGURATION
        config = new Config(new File(getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");
        staffModeConfig = new Config(new File(getDataFolder().getAbsolutePath() + "/staffmode/persist.yml"), "staffmode/persist.yml");
        settings = new Config(new File(getDataFolder().getAbsolutePath() + "/settings.yml", "settings.yml"));
        calendarConfig = new Config(new File(getDataFolder().getAbsolutePath() + "/calendar.yml"), "calendar.yml");

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
        moduleManager = new ModuleManager();
        handlerManager = new HandlerManager();
        runnableManager = new RunnableManager();
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

        // RUNNABLES
        runnableRegisterManager = new RunnableRegisterManager();
        runnableRegisterManager.runAll();

        // SCOREBOARD
        // handlerManager.getHandler(ScoreboardHandler.class).load();

    }

    @Override
    public void onDisable() {
        runnableRegisterManager.stopAll();
        calendar.stopRunner();
        moduleManager.unloadModules();
        handlerManager.getHandler(ScoreboardHandler.class).unload();
        instance = null;
    }

}
