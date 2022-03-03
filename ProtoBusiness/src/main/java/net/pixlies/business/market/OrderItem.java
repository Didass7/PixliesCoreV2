package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for order items
 * @author vPrototype_
 */
@AllArgsConstructor
public enum OrderItem {

    // MINING

    COBBLESTONE(Material.COBBLESTONE, 0, 0, 0),
    DEEPSLATE(Material.DEEPSLATE, 0, 1, 0),
    DIORITE(Material.DIORITE, 0, 2, 0),
    GRANITE(Material.GRANITE, 0, 3, 0),
    ANDESITE(Material.ANDESITE, 0, 4, 0),
    COAL(Material.COAL, 0, 5, 0),
    RAW_IRON(Material.RAW_IRON, 0, 0, 1),
    RAW_COPPER(Material.RAW_COPPER, 0, 1, 1),
    RAW_GOLD(Material.RAW_GOLD, 0, 2, 1),
    IRON_INGOT(Material.IRON_INGOT, 0, 3, 1),
    COPPER_INGOT(Material.COPPER_INGOT, 0, 4, 1),
    GOLD_INGOT(Material.GOLD_INGOT, 0, 5, 1),
    REDSTONE(Material.REDSTONE, 0, 0, 2),
    LAPIS_LAZULI(Material.LAPIS_LAZULI, 0, 1, 2),
    AMETHYST_SHARD(Material.AMETHYST_SHARD, 0, 2, 2),
    AMETHYST_BLOCK(Material.AMETHYST_BLOCK, 0, 3, 2),
    DIAMOND(Material.DIAMOND, 0, 4, 2),
    EMERALD(Material.EMERALD, 0, 5, 2),
    QUARTZ(Material.QUARTZ, 0, 0, 3),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK, 0, 1, 3),
    OBSIDIAN(Material.OBSIDIAN, 0, 2, 3),
    NETHERITE_SCRAP(Material.NETHERITE_SCRAP, 0, 3, 3),
    NETHERITE_INGOT(Material.NETHERITE_INGOT, 0, 4, 3),
    GLOWSTONE_DUST(Material.GLOWSTONE_DUST, 0, 5, 3);

    // FOODSTUFFS & PLANTS

    // BLOCKS

    // MOB DROPS

    // MISCELLANEOUS

    // TODO all the other items

    /**
     * Material of the item
     */
    @Getter private final Material material;

    /**
     * Page in the market menu
     */
    @Getter private final int page;

    /**
     * Position X in its page
     */
    @Getter private final int posX;

    /**
     * Position Y in its page
     */
    @Getter private final int posY;

    /**
     * Gets the items of a specific page
     * @param page page integer (starts at 0)
     * @return a list of all the items in given page
     */
    public List<OrderItem> getItemsOfPage(int page) {
        List<OrderItem> list = new ArrayList<>();
        for (OrderItem item : OrderItem.values()) {
            if (item.getPage() == page) list.add(item);
        }
        return list;
    }

}
