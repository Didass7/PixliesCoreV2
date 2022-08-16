package net.pixlies.core;

import lombok.Getter;
import net.pixlies.core.calendar.PixliesCalendar;
import net.pixlies.core.commands.PixliesCommandManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.database.MongoManager;
import net.pixlies.core.database.redis.RedisManager;
import net.pixlies.core.handlers.HandlerManager;
import net.pixlies.core.handlers.RegisterHandlerManager;
import net.pixlies.core.handlers.impl.PixlieMojiHandler;
import net.pixlies.core.handlers.impl.ScoreboardHandler;
import net.pixlies.core.listeners.ListenerManager;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.runnables.RunnableManager;
import net.pixlies.core.runnables.RunnableRegisterManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.pixlies.core.utils.FileUtils.saveIfNotExists;

public class Main extends JavaPlugin {

    @Getter private static Main instance;

    @Getter private MongoManager database;
    @Getter private RedisManager redisManager;
    @Getter private HandlerManager handlerManager;
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

        // DEPENDENCY CHECK
        this.getDescription().getDepend().forEach(pluginName -> {
            if (this.getServer().getPluginManager().getPlugin(pluginName) == null) {
                getLogger().severe(pluginName + " is not loaded. Please make sure " + pluginName + " is installed on the target server.");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        });

        // CONFIGURATION
        config =            new Config(new File(getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");
        staffModeConfig =   new Config(new File(getDataFolder().getAbsolutePath() + "/staffmode/persist.yml"), "staffmode/persist.yml");
        settings =          new Config(new File(getDataFolder().getAbsolutePath() + "/settings.yml"), "settings.yml");
        calendarConfig =    new Config(new File(getDataFolder().getAbsolutePath() + "/calendar.yml"), "calendar.yml");
        warpsConfig =       new Config(new File(getDataFolder().getAbsolutePath() + "/warps.yml"), "warps.yml");

        // CHECK LOBBY
        lobbyServer = config.getBoolean("commands.limitedCommands", false);

        // CONFIG INIT
        saveIfNotExists("staffmode/persist.yml");

        // LANGUAGE
        saveResource("languages/LANG_GER.yml", true);
        saveResource("languages/LANG_ENG.yml", true);
        saveResource("languages/LANG_FRA.yml", true);
        saveResource("languages/LANG_ARM.yml", true);
        saveResource("languages/LANG_LUX.yml", true);
        saveResource("languages/LANG_PER.yml", true);
        Lang.init();

        // HANDLERS
        handlerManager = new HandlerManager();
        runnableManager = new RunnableManager();
        new RegisterHandlerManager().registerAllHandlers();

        // DATABASE
        database = new MongoManager().init();
        redisManager = new RedisManager().init();

        // PIXLIES CALENDAR
        String[] date = calendarConfig.getString("date", "0/0/0").split("/");
        calendar = new PixliesCalendar(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        calendar.startRunner();

        // LISTENERS & COMMANDS
        ListenerManager.registerAllListeners();
        commandManager = new PixliesCommandManager();

        // RUNNABLES
        runnableRegisterManager = new RunnableRegisterManager();
        runnableRegisterManager.runAll();

        // CHAT
        handlerManager.getHandler(PixlieMojiHandler.class).loadEmojis();

    }

    @Override
    public void onDisable() {
        runnableRegisterManager.stopAll();
        calendar.stopRunner();
        handlerManager.getHandler(ScoreboardHandler.class).unload();
        instance = null;
    }

    public String getServerName() {
        return config.getString("server.name", "");
    }

}
