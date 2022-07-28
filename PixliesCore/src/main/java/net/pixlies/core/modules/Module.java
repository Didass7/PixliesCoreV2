package net.pixlies.core.modules;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public interface Module {

    default @Nonnull String getName() {
        return getJavaPlugin().getName();
    }

    default @Nullable Logger getLogger() {
        return getJavaPlugin().getLogger();
    }

    default @Nonnull String getPluginVersion() {
        return getJavaPlugin().getDescription().getVersion();
    }

    default boolean hasDependency(@Nonnull String dependency) {
        Validate.notNull(dependency, "The dependency cannot be null");

        if (getJavaPlugin().getName().equalsIgnoreCase(dependency)) {
            return true;
        }

        PluginDescriptionFile description = getJavaPlugin().getDescription();
        return description.getDepend().contains(dependency) || description.getSoftDepend().contains(dependency);
    }

    @NotNull
    JavaPlugin getJavaPlugin();

}
