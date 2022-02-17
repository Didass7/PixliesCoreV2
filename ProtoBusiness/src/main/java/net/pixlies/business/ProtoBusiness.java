package net.pixlies.business;

import lombok.Getter;
import net.pixlies.business.commands.CommandManager;
import net.pixlies.business.companies.CompanyManager;
import net.pixlies.business.database.MongoManager;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.modules.Module;

import java.io.File;

@Getter
public class ProtoBusiness extends Module {

    @Getter private static ProtoBusiness instance;

    private Config config;
    private MongoManager mongoManager;
    private CommandManager commandManager;
    private CompanyManager companyManager;

    @Override
    public void onLoad() {
        instance = this;

        config = new Config(new File(this.getModuleFolder(), "config.yml"), "config.yml");

        mongoManager = new MongoManager();
        mongoManager.init();

        commandManager = new CommandManager();
        commandManager.registerAllCommands();

        companyManager = new CompanyManager();
    }

    @Override
    public void onDrop() {
        commandManager.unregisterAllCommands();

        instance = null;
    }

}
