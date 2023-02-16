package net.pixlies.business;

import lombok.Getter;
import net.pixlies.business.commands.CommandManager;
import net.pixlies.business.handlers.HandlerManager;
import net.pixlies.business.listeners.ListenerManager;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.OrderBook;
import net.pixlies.business.threads.BalTopThread;
import net.pixlies.business.threads.EmbargoExpirationThread;
import net.pixlies.core.modules.Module;
import net.pixlies.core.modules.configuration.ModuleConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class ProtoBusinesss extends JavaPlugin implements Module {
    @Getter
    private static ProtoBusinesss instance;
    
    private ModuleConfig config;
    private ModuleConfig stats;
    private ModuleConfig log;
    
    private HandlerManager handlerManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    
    private BalTopThread balTopThread;
    private EmbargoExpirationThread embargoExpirationThread;
    
    @Override
    public void onEnable() {
        instance = this;
        
        config = new ModuleConfig(this, new File(this.getDataFolder(), "config.yml"), "config.yml");
        stats = new ModuleConfig(this, new File(this.getDataFolder(), "stats.yml"), "stats.yml");
        log = new ModuleConfig(this, new File(this.getDataFolder(), "log.yml"), "log.yml");
        
        this.saveResource("languages/LANG_ENG.yml", true);
        MarketLang.load();
        
        balTopThread = new BalTopThread();
        balTopThread.startThread();
    
        embargoExpirationThread = new EmbargoExpirationThread();
        embargoExpirationThread.startThread();
        
        handlerManager = new HandlerManager();
        handlerManager.registerAllHandlers();
        
        commandManager = new CommandManager();
        commandManager.registerAllCommands();
        
        OrderBook.loadAll();
        
        listenerManager = new ListenerManager();
        listenerManager.registerAllListeners();
    }
    
    @Override
    public void onDisable() {
        commandManager.unregisterAllCommands();
        balTopThread.stopThread();
        
        MarketProfile.backupAll();
        OrderBook.backupAll();
        
        instance = null;
    }
    
    public void logInfo(String message) {
        getLogger().info(message);
    }
    
    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
