package net.pixlies.lobby.utils;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

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

                    // TODO: add more features
                    ChestGui gui = new ChestGui(5, CC.format("Server Selector"));
                    StaticPane pane = new StaticPane(8, 3);
                    gui.setOnGlobalClick(e -> e.setCancelled(true));

                    // EARTH HEAD
                    GuiItem earthHead = new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, 1)
                            .setDisplayName(CC.format("&aEarth"))
                            .addLoreLine(CC.format("&7The only Earth without global warming."))
                            // earth head texture
                            .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRkZTU5NGRlYWQ4OGIzNWJjMjFhZDFhYjIzOGRjYWU0MTEyNTNlMzRhNTg1ZDkyNTI1OGNlNjc0YzY0MjYxNyJ9fX0=")
                            .build(), e -> {

                        HumanEntity humanEntity = e.getWhoClicked();

                        if (humanEntity instanceof Player player) {
                            player.performCommand("play Earth");
                        }

                    });
                    pane.addItem(earthHead, 4, 1);

                    gui.addPane(pane);
                    gui.show(event.getPlayer());

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