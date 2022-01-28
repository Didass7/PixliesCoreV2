package net.pixlies.core.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.ChatHandler;
import net.pixlies.core.handlers.impl.CosmeticsHandler;

public class RegisterHandlerManager {

    private static final Main instance = Main.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            ChatHandler.class,
            CosmeticsHandler.class
    );

    public void registerAllHandlers() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }

}
