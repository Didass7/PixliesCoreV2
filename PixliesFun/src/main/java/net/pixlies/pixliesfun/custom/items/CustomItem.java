package net.pixlies.pixliesfun.custom.items;

import net.pixlies.core.Main;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.pixliesfun.PixliesFun;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Custom items.
 * @author Dynmie
 */
public abstract class CustomItem {

    private static final PixliesFun instance = PixliesFun.getInstance();
    private static final Main pixlies = Main.getInstance();

    /**
     * Build the item into an ItemStack.
     * @return The built custom item.
     */
    public ItemStack build() {

        ItemStack item = new ItemBuilder(getMaterial())
                .setDisplayName(getDisplayName())
                .addLoreAll(getLore())
                .build();

        PersistentDataContainer container = item.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(pixlies, "PixliesFunItem");
        container.set(key, PersistentDataType.STRING, getIdentifier());

        return item;

    }

    /**
     * Ran when the player interacts with this item in the main hand.
     * Override this to change the function.
     * @param event the player interact event.
     */
    public void onInteractItem(@NotNull PlayerInteractEvent event) {

    }

    /**
     * Ran when the player interacts with this item in the main hand.
     * Override this to change the function.
     * @param event the player sneak event.
     */
    public void onToggleSneakItem(@NotNull PlayerToggleSneakEvent event) {

    }

    /**
     * Ran when the player drops the item on the ground.
     * Override this to change the function.
     * @param event the item drop event.
     */
    public void onDropItem(@NotNull PlayerDropItemEvent event) {

    }

    /**
     * Gets the display name.
     * @return The display name of the custom item.
     */
    public abstract @NotNull String getDisplayName();

    /**
     * Gets the lore.
     * @return The lore in a list.
     */
    public abstract @NotNull List<String> getLore();

    /**
     * Get the material.
     * @return Get the material of the custom item.
     */
    public abstract @NotNull Material getMaterial();

    /**
     * Get the identifier of the custom item.
     * @return The identifier of the custom item.
     */
    public abstract @NotNull String getIdentifier();

    /**
     * If the custom item is stackable.
     * @return True if the item is stackable, false if it isn't.
     */
    public abstract boolean isStackable();

}
