package net.pixlies.nations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.pixlies.nations.commands.CommandManager;
import net.pixlies.nations.events.ListenerManager;
import net.pixlies.nations.handlers.HandlerManager;
import net.pixlies.nations.handlers.RegisterHandlerManager;
import net.pixlies.core.modules.Module;

public class Nations extends Module {

    private static Nations instance;

    private HandlerManager handlerManager;
    private ListenerManager listenerManager;
    private CommandManager commandManager;

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    @Override
    public void onLoad() {
        instance = this;

        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAllHandlers();

        listenerManager = new ListenerManager();
        listenerManager.registerAllListeners();

        commandManager = new CommandManager();
        commandManager.registerAllCommands();

    }

    @Override
    public void onDrop() {
        listenerManager.unregisterAllListeners();
        commandManager.unregisterAllCommands();
        instance = null;
    }

    public Gson getGson() {
        return gson;
    }

    public HandlerManager getHandlerManager() {
        return handlerManager;
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public static Nations getInstance() {
        return instance;
    }

}
