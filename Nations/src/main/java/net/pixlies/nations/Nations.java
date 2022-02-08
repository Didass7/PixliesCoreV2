package net.pixlies.nations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.pixlies.core.modules.Module;
import net.pixlies.nations.commands.CommandManager;
import net.pixlies.nations.events.ListenerManager;
import net.pixlies.nations.handlers.HandlerManager;
import net.pixlies.nations.handlers.RegisterHandlerManager;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.NationManager;

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

        // HANDLERS & MANAGERS
        handlerManager = new HandlerManager();
        new RegisterHandlerManager().registerAllHandlers();

        // LISTENERS
        listenerManager = new ListenerManager();
        listenerManager.registerAllListeners();

        // COMMANDS
        commandManager = new CommandManager();
        commandManager.registerAllCommands();

    }

    @Override
    public void onDrop() {

        // COMMANDS & LISTENERS
        listenerManager.unregisterAllListeners();
        commandManager.unregisterAllCommands();
        instance = null;

        // NATIONS
        for (Nation nation : NationManager.nations.values()) {
            nation.backup();
        }

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
