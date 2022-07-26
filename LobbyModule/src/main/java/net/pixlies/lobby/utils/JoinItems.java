package net.pixlies.lobby.utils;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class JoinItems {

    private static final @Getter List<LobbyItem> lobbyItems = ImmutableList.of(
            // 0 1 [C] 3 [S] 5 [V] 7 8

            // Cosmetics
            new LobbyItem(2, new ItemBuilder(Material.NETHERITE_HELMET)
                    .setDisplayName(CC.format("&dCosmetics"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build(), true) {
                @Override
                public void onClick(@NotNull PlayerInteractEvent event) {
                    // TODO
                }
            },

            // Server Selector
            new LobbyItem(4, new ItemBuilder(Material.GLOBE_BANNER_PATTERN)
                    .setDisplayName(CC.format("&bServer Selector"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .setGlow()
                    .build(), true) {
                @Override
                public void onClick(@NotNull PlayerInteractEvent event) {
                    // TODO
                }
            },

            // Grappling Hook
            new LobbyItem(6, new ItemBuilder(Material.FISHING_ROD)
                    .setDisplayName(CC.format("&6Grappling Hook"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build(), true) {

                @Override
                public void onClick(@NotNull PlayerInteractEvent event) {
                    // TODO
                }
            }

    );

    public static void give(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        for (LobbyItem lobbyItem : lobbyItems) {
            if (lobbyItem.isGiveOnJoin()) {
                inventory.setItem(lobbyItem.getSlot(), lobbyItem.getItemStack());
            }
        }

    }

}