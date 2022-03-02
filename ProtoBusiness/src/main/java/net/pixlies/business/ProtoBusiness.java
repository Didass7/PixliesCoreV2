package net.pixlies.business;

import lombok.Getter;
import net.pixlies.business.commands.CommandManager;
import net.pixlies.business.companies.CompanyManager;
import net.pixlies.business.configuration.Config;
import net.pixlies.business.database.MongoManager;
import net.pixlies.business.handlers.HandlerManager;
import net.pixlies.business.handlers.RegisterHandlerManager;
import net.pixlies.business.market.MarketManager;
import net.pixlies.business.runnables.RunnableManager;
import net.pixlies.core.modules.Module;

import java.io.File;

@Getter
public class ProtoBusiness extends Module {

    @Getter private static ProtoBusiness instance;

    private Config config;
    private Config stats;

    private MongoManager mongoManager;
    private HandlerManager handlerManager;
    private CommandManager commandManager;
    private CompanyManager companyManager;
    private MarketManager marketManager;
    private RunnableManager runnableManager;

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

        companyManager = new CompanyManager();
        marketManager = new MarketManager();

        runnableManager = new RunnableManager();
        runnableManager.registerAll();
    }

    @Override
    public void onDrop() {
        commandManager.unregisterAllCommands();
        marketManager.backupAll();

        instance = null;
    }

}
