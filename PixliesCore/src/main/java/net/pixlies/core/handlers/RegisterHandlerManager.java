package net.pixlies.core.handlers;

import com.google.common.collect.ImmutableList;
import net.pixlies.core.Main;

public class RegisterHandlerManager {

    private static final Main instance = Main.getInstance();

    private final ImmutableList<Class<? extends Handler>> handlers = ImmutableList.of(
            // ADD HANDLERS HERE AS CLASS (SomeHandler.class)
    );

    public void registerAllHandlers() {
        handlers.forEach(clazz -> instance.getHandlerManager().register(clazz));
    }

}
