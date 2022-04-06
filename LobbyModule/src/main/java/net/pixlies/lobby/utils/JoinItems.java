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

            // Visibility Toggle ON
            getViewOtherItem(true),
            getViewOtherItem(false)

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

    public static LobbyItem getViewOtherItem(boolean state) {

        if (state) {
            return new LobbyItem(6, new ItemBuilder(Material.LIME_DYE)
                    .setDisplayName(CC.format("&aView Others §8(Left Click)"))
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build(), true) {

                @Override
                public void onClick(@NotNull PlayerInteractEvent event) {
                    Player player = event.getPlayer();
                    PlayerInventory inventory = player.getInventory();

                    inventory.setItem(6, getViewOtherItem(false).getItemStack());
                    // TODO
                }

            };
        }

        return new LobbyItem(6, new ItemBuilder(Material.GRAY_DYE)
                .setDisplayName(CC.format("&7View Others §8(Left Click)"))
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build(), false) {

            @Override
            public void onClick(@NotNull PlayerInteractEvent event) {
                Player player = event.getPlayer();
                PlayerInventory inventory = player.getInventory();

                inventory.setItem(6, getViewOtherItem(true).getItemStack());
                // TODO
            }

        };

    }
}