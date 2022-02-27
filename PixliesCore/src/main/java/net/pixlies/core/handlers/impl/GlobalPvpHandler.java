package net.pixlies.core.handlers.impl;

import lombok.Getter;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.Handler;

public class GlobalPvpHandler implements Handler {

    private static final Main instance = Main.getInstance();

    @Getter private volatile boolean globalPvpEnabled = true;

    public void setGlobalPvp(boolean value) {
        globalPvpEnabled = value;
        instance.getSettings().set("pvpEnabled", value);
        instance.getSettings().save();
    }

}
