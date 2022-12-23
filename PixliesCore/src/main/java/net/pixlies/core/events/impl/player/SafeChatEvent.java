package net.pixlies.core.events.impl.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SafeChatEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    
    @Getter
    @Setter
    private Player player;
    
    @Getter
    @Setter
    private String format;
    
    @Getter
    @Setter
    private String message;
    
    @Getter
    @Setter
    private Set<Player> recipients;
    
    @Getter
    @Setter
    private boolean cancelled;
    
    public SafeChatEvent(boolean async, Player player, String format, String message, Set<Player> recipients) {
        super(async);
        this.player = player;
        this.format = format;
        this.message = message;
        this.recipients = recipients;
        this.cancelled = false;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
