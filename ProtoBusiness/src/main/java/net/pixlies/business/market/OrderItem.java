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

    // MINERALS

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
    GLOWSTONE_DUST(Material.GLOWSTONE_DUST, 0, 5, 3),

    // FOODSTUFFS & PLANTS

    WHEAT_SEEDS(Material.WHEAT_SEEDS, 1, 0, 0),
    WHEAT(Material.WHEAT, 1, 1, 0),
    PUMPKIN(Material.PUMPKIN, 1, 2, 0),
    MELON_SLICE(Material.MELON_SLICE, 1, 3, 0),
    CARROT(Material.CARROT, 1, 4, 0),
    POTATO(Material.POTATO, 1, 5, 0),
    HONEYCOMB(Material.HONEYCOMB, 1, 6, 0),
    BEETROOT(Material.BEETROOT, 1, 0, 1),
    COCOA_BEANS(Material.COCOA_BEANS, 1, 1, 1),
    APPLE(Material.APPLE, 1, 2, 1),
    GOLDEN_APPLE(Material.GOLDEN_APPLE, 1, 3, 1),
    ENCHANTED_GOLDEN_APPLE(Material.ENCHANTED_GOLDEN_APPLE, 1, 4, 1),
    MILK_BUCKET(Material.MILK_BUCKET, 1, 5, 1),
    HONEY_BOTTLE(Material.HONEY_BOTTLE, 1, 6, 1),
    PORKCHOP(Material.REDSTONE, 1, 0, 2),
    COOKED_PORKCHOP(Material.LAPIS_LAZULI, 1, 1, 2),
    BEEF(Material.AMETHYST_SHARD, 1, 2, 2),
    COOKED_BEEF(Material.AMETHYST_BLOCK, 1, 3, 2),
    CHICKEN(Material.DIAMOND, 1, 4, 2),
    COOKED_CHICKEN(Material.EMERALD, 1, 5, 2),
    SWEET_BERRIES(Material.SWEET_BERRIES, 1, 6, 2),
    MUTTON(Material.QUARTZ, 1, 0, 3),
    COOKED_MUTTON(Material.QUARTZ_BLOCK, 1, 1, 3),
    RABBIT(Material.OBSIDIAN, 1, 2, 3),
    COOKED_RABBIT(Material.NETHERITE_SCRAP, 1, 3, 3),
    RED_MUSHROOM(Material.NETHERITE_INGOT, 1, 4, 3),
    BROWN_MUSHROOM(Material.GLOWSTONE_DUST, 1, 5, 3),
    GLOW_BERRIES(Material.GLOW_BERRIES, 1, 6, 3),
    COD(Material.QUARTZ, 1, 0, 4),
    SALMON(Material.QUARTZ_BLOCK, 1, 1, 4),
    COOKED_COD(Material.OBSIDIAN, 1, 2, 4),
    COOKED_SALMON(Material.NETHERITE_SCRAP, 1, 3, 4),
    SUGAR_CANE(Material.SUGAR_CANE, 1, 4, 4),
    CACTUS(Material.CACTUS, 1, 5, 4),
    LILY_PAD(Material.LILY_PAD, 1, 6, 4);

    // BLOCKS

    // MOB DROPS

    // MISCELLANEOUS

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
