package net.pixlies.core.handlers.impl;

import net.pixlies.core.handlers.Handler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Handler {
    
    private final Map<UUID, UUID> replyTargets = new ConcurrentHashMap<>();
    // TODO: Socialspy

    public Player getReplyTarget(UUID uuid) {
        if (replyTargets.get(uuid) == null) return null;
        return Bukkit.getPlayer(replyTargets.get(uuid));
    }

    public void setReplyTarget(UUID uuid, UUID target) {
        replyTargets.remove(uuid);
        replyTargets.put(uuid, target);
    }

    // TODO: Message sound
    
}
