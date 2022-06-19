package net.pixlies.business;

import lombok.Getter;
import net.pixlies.business.commands.CommandManager;
import net.pixlies.business.configuration.Config;
import net.pixlies.business.database.MongoManager;
import net.pixlies.business.handlers.HandlerManager;
import net.pixlies.business.handlers.RegisterHandlerManager;
import net.pixlies.business.listeners.ListenerManager;
import net.pixlies.business.market.MarketManager;
import net.pixlies.business.market.orders.OrderBook;
import net.pixlies.core.modules.Module;
import org.bukkit.Bukkit;

import java.io.File;

@Getter
public class ProtoBusiness extends Module {

    @Getter private static ProtoBusiness instance;

    private Config config;
    private Config stats;

    private MongoManager mongoManager;
    private HandlerManager handlerManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private MarketManager marketManager;

    @Override
    public void onLoad() {
        instance = this;

        config = new Config(new File(this.getModuleFolder(), "config.yml"), "config.yml");
        stats = new Config(new File(this.getModuleFolder(), "stats.yml"), "stats.yml");

        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAllHandlers();

        mongoManager = new MongoManager();
        mongoManager.init();

        commandManager = new CommandManager();
        commandManager.registerAllCommands();

        marketManager = new MarketManager();
        Bukkit.getLogger().warning("Books loaded: " + marketManager.getBooks().size());
        for (OrderBook book : marketManager.getBooks().values()) {
            Bukkit.getLogger().warning(book.getItem().getName());
        }

        listenerManager = new ListenerManager();
        listenerManager.registerAllListeners();
    }

    @Override
    public void onDrop() {
        commandManager.unregisterAllCommands();

        marketManager.backupAll();

        instance = null;
    }

}
