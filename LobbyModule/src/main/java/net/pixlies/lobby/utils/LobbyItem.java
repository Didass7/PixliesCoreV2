package net.pixlies.lobby.utils;

import lombok.Data;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@Data
public class LobbyItem {

    private final int slot;
    private final ItemStack itemStack;

    public void onClick(PlayerInteractEvent event) {

    }

}
