package net.pixlies.core.events.impl.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PostSafeChatEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();
    
    @Getter
    @Setter
    private Player player;
    
    @Getter
    @Setter
    private String message;
    
    @Getter
    @Setter
    private String formattedMessage;
    
    @Getter
    @Setter
    private Set<Player> recipients;
    
    public PostSafeChatEvent(boolean async, Player player, String message, String formattedMessage, Set<Player> recipients) {
        super(async);
        this.player = player;
        this.message = message;
        this.formattedMessage = formattedMessage;
        this.recipients = recipients;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
