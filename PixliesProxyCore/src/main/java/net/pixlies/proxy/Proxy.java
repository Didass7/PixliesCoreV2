package net.pixlies.proxy;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class Proxy extends Plugin {

    @Getter static Proxy instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

}
