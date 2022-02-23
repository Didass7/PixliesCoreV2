package net.pixlies.pixliesfun.custom.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.Main;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.pixliesfun.PixliesFun;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Custom items.
 * @author Dynmie
 */
@Data
@AllArgsConstructor
public class CustomItem {

    private static final PixliesFun instance = PixliesFun.getInstance();
    private static final Main pixlies = Main.getInstance();

    private String displayName;
    private List<String> lore;
    private Material material;
    private String identifier;
    private boolean stackable;

    /**
     * Run this when the player interacts with this item.
     * @param event the player interact event.
     */
    public void onInteractItem(@NotNull PlayerInteractEvent event) {

    }

    /**
     * Build the item into an ItemStack.
     * @return The built custom item.
     */
    public ItemStack build() {

        ItemStack item = new ItemBuilder(material)
                .setDisplayName(displayName)
                .addLoreAll(lore)
                .build();

        PersistentDataContainer container = item.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(pixlies, "PixliesFunItem");
        container.set(key, PersistentDataType.STRING, identifier);

        return item;

    }

}
