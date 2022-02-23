package net.pixlies.pixliesfun;

import lombok.Getter;
import net.pixlies.core.modules.Module;

public class PixliesFun extends Module {

    @Getter private static PixliesFun instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDrop() {
        instance = null;
    }

}
