package net.pixlies.proxy;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.pixlies.proxy.commands.CommandManager;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.database.MongoManager;
import net.pixlies.proxy.database.redis.RedisManager;
import net.pixlies.proxy.handlers.HandlerManager;
import net.pixlies.proxy.handlers.RegisterHandlerManager;
import net.pixlies.proxy.handlers.impl.AutoAnnounceHandler;
import net.pixlies.proxy.listeners.ListenerManager;
import net.pixlies.proxy.localization.Lang;
import net.pixlies.proxy.queue.QueueManager;
import net.pixlies.proxy.runnables.RunnableManager;
import net.pixlies.proxy.utils.FileUtils;

import java.io.File;

@Getter
public class PixliesProxy extends Plugin {

    @Getter private static PixliesProxy instance;

    private MongoManager mongoManager;
    private RedisManager redisManager;
    private QueueManager queueManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private HandlerManager handlerManager;

    private Config config;

    @Override
    public void onEnable() {

        // INSTANCE
        instance = this;

        // CONFIGS
        config = new Config(new File(getDataFolder().getAbsolutePath() + "/config.yml"), "config.yml");

        // LANGUAGE
        FileUtils.saveResource("languages/LANG_ENG.yml", true);
        Lang.init();

        // MANAGERS & HANDLERS
        redisManager = new RedisManager();
        mongoManager = new MongoManager();
        redisManager.init();
        mongoManager.init();
        queueManager = new QueueManager();
        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAll();
        new RunnableManager().runAll();

        // COMMANDS
        commandManager = new CommandManager();
        commandManager.registerAll();

        // LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerAll();

        handlerManager.getHandler(AutoAnnounceHandler.class).loadMessages();
        queueManager.setupQueues();

    }

    @Override
    public void onDisable() {
        instance = null;
    }

}
