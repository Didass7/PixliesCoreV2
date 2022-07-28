package net.pixlies.pixliesfun;

import lombok.Getter;
import net.pixlies.core.modules.Module;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PixliesFun extends JavaPlugin implements Module {

    @Getter private static PixliesFun instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

}
