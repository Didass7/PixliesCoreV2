package net.pixlies.proxy.handlers.impl;

import net.pixlies.proxy.PixliesProxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.Handler;
import net.pixlies.proxy.utils.CC;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Cool auto announce system.
 * @author Dynmie
 */
public class AutoAnnounceHandler implements Handler {

    private static final PixliesProxy instance = PixliesProxy.getInstance();
    private static final Config config = instance.getConfig();

    private final List<String> messages = new LinkedList<>();

    public @Nullable String getFirstAndReplace() {
        if (messages.isEmpty()) return null;
        String message = messages.get(0);
        String prefix = config.getConfig().getString("autoannounce.prefix", "");
        messages.remove(message);
        messages.add(message);
        return CC.format(prefix + message);
    }

    public void loadMessages() {
        if (!this.isAutoAnnounceEnabled()) return;
        List<String> messages = config.getConfig().getStringList("autoannounce.messages");
        this.messages.clear();
        this.messages.addAll(messages);
    }

    public int getDelay() {
        return config.getConfig().getInt("autoannounce.delay", 240);
    }

    /**
     * Get all loaded messages
     * @return all loaded messages
     */
    public @NotNull Collection<String> getMessages() {
        return messages;
    }

    public boolean isAutoAnnounceEnabled() {
        return config.getConfig().getBoolean("autoannounce.enabled", true);
    }

}
