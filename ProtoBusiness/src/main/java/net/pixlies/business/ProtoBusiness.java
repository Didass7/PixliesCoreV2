package net.pixlies.business;

import lombok.Getter;
import net.pixlies.business.commands.CommandManager;
import net.pixlies.business.handlers.HandlerManager;
import net.pixlies.business.listeners.ListenerManager;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.core.modules.Module;
import net.pixlies.core.modules.configuration.ModuleConfig;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
public class ProtoBusiness extends JavaPlugin implements Module {
    @Getter
    private static ProtoBusiness instance;
    
    private ModuleConfig config;
    private ModuleConfig stats;
    
    private HandlerManager handlerManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        config = new ModuleConfig(this, new File(this.getDataFolder(), "config.yml"), "config.yml");
        stats = new ModuleConfig(this, new File(this.getDataFolder(), "stats.yml"), "stats.yml");
        
        this.saveResource("languages/LANG_ENG.yml", true);
        MarketLang.load();
        
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
        
        MarketProfile.backupAll();
        OrderBook.backupAll();
        
        instance = null;
    }
    
    public void logInfo(String message) {
        getLogger().info("[ProtoBusiness] " + message);
    }
    
    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }
}
