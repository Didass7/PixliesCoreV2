package net.pixlies.lobby.utils;

import lombok.Data;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Data
public class LobbyItem {

    private final int slot;
    private final ItemStack itemStack;
    private final boolean giveOnJoin;

    public void onClick(@NotNull PlayerInteractEvent event) {

    }

}
