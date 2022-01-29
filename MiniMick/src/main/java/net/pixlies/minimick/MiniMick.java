package net.pixlies.minimick;

import net.pixlies.core.modules.Module;

public class MiniMick extends Module {

    private static MiniMick instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDrop() {
        instance = null;
    }
}
