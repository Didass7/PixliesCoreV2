package net.pixlies.business;

import lombok.Getter;
import net.pixlies.business.commands.CommandManager;
import net.pixlies.business.database.MongoManager;
import net.pixlies.business.handlers.HandlerManager;
import net.pixlies.business.handlers.RegisterHandlerManager;
import net.pixlies.business.listeners.ListenerManager;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketManager;
import net.pixlies.core.modules.Module;
import net.pixlies.core.modules.configuration.ModuleConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class ProtoBusiness extends JavaPlugin implements Module {

    @Getter private static ProtoBusiness instance;

    private ModuleConfig config;
    private ModuleConfig stats;

    private MongoManager mongoManager;
    private HandlerManager handlerManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private MarketManager marketManager;

    @Override
    public void onEnable() {
        instance = this;

        config = new ModuleConfig(this, new File(this.getDataFolder(), "config.yml"), "config.yml");
        stats = new ModuleConfig(this, new File(this.getDataFolder(), "stats.yml"), "stats.yml");

        this.saveResource("languages/LANG_ENG.yml", true);
        MarketLang.load();

        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAllHandlers();

        mongoManager = new MongoManager();
        mongoManager.init();

        commandManager = new CommandManager();
        commandManager.registerAllCommands();

        marketManager = new MarketManager();
        Bukkit.getLogger().warning("OrderBooks loaded: " + marketManager.getBooks().size());

        listenerManager = new ListenerManager();
        listenerManager.registerAllListeners();
    }

    @Override
    public void onDisable() {
        commandManager.unregisterAllCommands();

        marketManager.backupAll();

        instance = null;
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
