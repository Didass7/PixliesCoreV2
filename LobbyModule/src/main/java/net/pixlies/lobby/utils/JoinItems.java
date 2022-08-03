package net.pixlies.lobby.utils;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.modules.configuration.ModuleConfig;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.lobby.Lobby;
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

    private static final ModuleConfig config = Lobby.getInstance().getConfig();

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
                    Player player = event.getPlayer();

                    ChestGui gui = new ChestGui(4, CC.format("Server Selector"));
                    StaticPane pane = new StaticPane(9, 4);
                    gui.setOnGlobalClick(e -> e.setCancelled(true));


                    // EARTH SERVER
                    int onlinePlayersEarth = 1;
                    try {
                        int playersOnline = Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%bungee_earth%"));
                        if (playersOnline > 1) {
                            onlinePlayersEarth = playersOnline;
                        }
                    } catch (NumberFormatException ignored) {
                        // ignored
                    }
                    GuiItem earthHead = new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, onlinePlayersEarth)
                            .setDisplayName(CC.format("&aEarth"))
                            .addLoreLine(CC.format("&7The only Earth without global warming."))
                            .addLoreLine(CC.format(PlaceholderAPI.setPlaceholders(player, "&7Online: %bungee_earth%/" + config.getInt("servers.Earth.max-players", 200))))
                            .addLoreLine("")
                            .addLoreLine(CC.format("&aLeft Click&7 to join the queue!"))
                            // earth head texture
                            .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmRkZTU5NGRlYWQ4OGIzNWJjMjFhZDFhYjIzOGRjYWU0MTEyNTNlMzRhNTg1ZDkyNTI1OGNlNjc0YzY0MjYxNyJ9fX0=")
                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                            .setGlow()
                            .build(), e -> player.performCommand("play Earth"));
                    pane.addItem(earthHead, 4, 1);

                    // PVP WARP
                    GuiItem pvpHead = new GuiItem(new ItemBuilder(Material.NETHERITE_SWORD, 1)
                            .setDisplayName(CC.format("&cPvP"))
                            .addLoreLine(CC.format("&7Warp to the PvP arena."))
                            .addLoreLine("")
                            .addLoreLine(CC.format("&aLeft Click&7 to warp!"))
                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                            .build(), e -> player.performCommand("pvp"));
                    pane.addItem(pvpHead, 2, 2);

                    // DEV SERVER
                    GuiItem devHead = new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, 1)
                            .setDisplayName(CC.format("&eDevelopment"))
                            .addLoreLine(CC.format("&7The only place where we can be free."))
                            .addLoreLine(CC.format(PlaceholderAPI.setPlaceholders(player, "&7Online: %bungee_development%/" + config.getInt("servers.Earth.max-players", 200))))
                            .addLoreLine("")
                            .addLoreLine(CC.format("&aLeft Click&7 to join the queue!"))
                            // gaming computer head texture
                            .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjhiY2NkOGQ5MTZhMTk2MGVkMzA0Zjk5ZDgxYjliZTY3NmFjODIxYjFhMWMzNTZiMzgwODMwNTc1YTQ0NDRlMyJ9fX0=")
                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                            .build(), e -> player.performCommand("play Development"));
                    // if player has access to join dev server
                    if (player.hasPermission("pixlies.servers.development")) {
                        pane.addItem(devHead, 8, 3);
//                        pane.addItem(devHead, 0, 0);
                    }

                    // ABOUT BOOK
                    GuiItem aboutBook = new GuiItem(new ItemBuilder(Material.BOOK, 1)
                            .setDisplayName(CC.format("&6Info"))
                            .addLoreLine(CC.format("&7Join our Discord at pixlies.net/discord."))
                            .addLoreLine("")
                            .addLoreLine(CC.format("&aLeft Click&7 for more info!"))
                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                            .build(), e -> player.performCommand("discord"));
                    pane.addItem(aboutBook, 6, 2);

                    pane.setOnClick(e -> e.getWhoClicked().closeInventory());
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
                    // Already handled.
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