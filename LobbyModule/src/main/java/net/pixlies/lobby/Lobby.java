package net.pixlies.lobby;

import lombok.Getter;
import net.pixlies.core.modules.Module;

public class Lobby extends Module {

    @Getter static Lobby instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDrop() {
        instance = null;
    }

}
