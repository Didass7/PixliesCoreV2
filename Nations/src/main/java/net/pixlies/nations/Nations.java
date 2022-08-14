package net.pixlies.nations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ScoreboardHandler;
import net.pixlies.core.modules.Module;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.nations.commands.CommandManager;
import net.pixlies.nations.database.MongoManager;
import net.pixlies.nations.listeners.ListenerManager;
import net.pixlies.nations.handlers.HandlerManager;
import net.pixlies.nations.handlers.RegisterHandlerManager;
import net.pixlies.nations.locale.Lang;
import net.pixlies.nations.nations.NationManager;
import net.pixlies.nations.integrations.NationsPlaceholderExpansion;
import net.pixlies.nations.runnables.RunnableManager;
import net.pixlies.nations.scoreboard.ScoreboardAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class Nations extends JavaPlugin implements Module {

    @Getter private static Nations instance;

    private ModuleConfig config;

    private HandlerManager handlerManager;
    private ListenerManager listenerManager;
    private CommandManager commandManager;
    private NationManager nationManager;
    private MongoManager mongoManager;
    private RunnableManager runnableManager;

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    @Override
    public void onEnable() {

        instance = this;

        this.getDescription().getDepend().forEach(pluginName -> {
            if (this.getServer().getPluginManager().getPlugin(pluginName) == null) {
                getLogger().severe("Couldn't find" + pluginName + "! Please make sure " + pluginName + " is installed and enabled on the server.");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        });

        // CONFIGURATION
        config = new ModuleConfig(this, new File(this.getDataFolder(), "config.yml"), "config.yml");

        // LANGUAGE
        saveResource("languages/LANG_ENG.yml", true);
        Lang.load();

        // HANDLERS & MANAGERS
        mongoManager = new MongoManager();
        mongoManager.init();
        handlerManager = new HandlerManager();
        nationManager = new NationManager();
        runnableManager = new RunnableManager();
        runnableManager.registerAll();

        new RegisterHandlerManager().registerAllHandlers();

        // LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerAllListeners();

        // COMMANDS
        commandManager = new CommandManager();
        commandManager.registerAllCommands();

        // SCOREBOARD
        Main.getInstance().getHandlerManager().getHandler(ScoreboardHandler.class).load(new ScoreboardAdapter());

        // PLACEHOLDERS
        new NationsPlaceholderExpansion().register();

        // LOADING
        nationManager.refreshNations();

    }

    @Override
    public void onDisable() {

        // COMMANDS & LISTENERS
        nationManager.backupAll();
        listenerManager.unregisterAllListeners();
        commandManager.unregisterAllCommands();
        runnableManager.unregisterAll();
        instance = null;

    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
