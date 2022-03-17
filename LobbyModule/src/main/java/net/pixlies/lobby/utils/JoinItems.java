package net.pixlies.lobby.utils;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public final class JoinItems {

    private static final @Getter List<LobbyItem> lobbyItems = ImmutableList.of(
            // 0 1 [C] 3 [S] 5 [V] 7 8

            // Cosmetics
            new LobbyItem(2, new ItemBuilder(Material.NETHERITE_HELMET)
                    .setDisplayName(CC.format("&dCosmetics"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build()) {
                @Override
                public void onClick(PlayerInteractEvent event) {
                    // TODO
                }
            },

            // Server Selector
            new LobbyItem(4, new ItemBuilder(Material.GLOBE_BANNER_PATTERN)
                    .setDisplayName(CC.format("&bServer Selector"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setGlow()
                    .build()) {
                @Override
                public void onClick(PlayerInteractEvent event) {
                    // TODO
                }
            },

            // Visibility Toggle ON
            new LobbyItem(6, new ItemBuilder(Material.LIME_DYE)
                    .setDisplayName(CC.format("&aView Others"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build()) {
                @Override
                public void onClick(PlayerInteractEvent event) {
                    // TODO
                }
            },

            // Visibility Toggle OFF
            new LobbyItem(6, new ItemBuilder(Material.GRAY_DYE)
                    .setDisplayName(CC.format("&7View Others"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build()) {
                @Override
                public void onClick(PlayerInteractEvent event) {
                    // TODO
                }
            }

    );

    public static void give(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        for (LobbyItem lobbyItem : lobbyItems) {
            inventory.setItem(lobbyItem.getSlot(), lobbyItem.getItemStack());
        }

    }

}
