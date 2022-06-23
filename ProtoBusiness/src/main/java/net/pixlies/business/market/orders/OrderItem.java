package net.pixlies.business.market.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum for order items
 * @author vPrototype_
 */
@AllArgsConstructor
public enum OrderItem {

    // TODO: TERRACOTTA, WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, WHITE_WOOL, SHULKER_BOX

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
    PORKCHOP(Material.PORKCHOP, 1, 0, 2),
    COOKED_PORKCHOP(Material.COOKED_PORKCHOP, 1, 1, 2),
    BEEF(Material.BEEF, 1, 2, 2),
    COOKED_BEEF(Material.COOKED_BEEF, 1, 3, 2),
    CHICKEN(Material.CHICKEN, 1, 4, 2),
    COOKED_CHICKEN(Material.COOKED_CHICKEN, 1, 5, 2),
    SWEET_BERRIES(Material.SWEET_BERRIES, 1, 6, 2),
    MUTTON(Material.MUTTON, 1, 0, 3),
    COOKED_MUTTON(Material.COOKED_MUTTON, 1, 1, 3),
    RABBIT(Material.RABBIT, 1, 2, 3),
    COOKED_RABBIT(Material.COOKED_RABBIT, 1, 3, 3),
    RED_MUSHROOM(Material.RED_MUSHROOM, 1, 4, 3),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 1, 5, 3),
    GLOW_BERRIES(Material.GLOW_BERRIES, 1, 6, 3),
    COD(Material.COD, 1, 0, 4),
    SALMON(Material.SALMON, 1, 1, 4),
    COOKED_COD(Material.COOKED_COD, 1, 2, 4),
    COOKED_SALMON(Material.COOKED_SALMON, 1, 3, 4),
    SUGAR_CANE(Material.SUGAR_CANE, 1, 4, 4),
    CACTUS(Material.CACTUS, 1, 5, 4),
    LILY_PAD(Material.LILY_PAD, 1, 6, 4),

    // BLOCKS

    DIRT(Material.DIRT, 2, 0, 0),
    GRASS_BLOCK(Material.GRASS_BLOCK, 2, 1, 0),
    MOSS_BLOCK(Material.MOSS_BLOCK, 2, 2, 0),
    MYCELIUM(Material.MYCELIUM, 2, 3, 0),
    SAND(Material.SAND, 2, 4, 0),
    RED_SAND(Material.RED_SAND, 2, 5, 0),
    GLASS(Material.GLASS, 2, 6, 0),
    OAK_LOG(Material.OAK_LOG, 2, 0, 1),
    SPRUCE_LOG(Material.SPRUCE_LOG, 2, 1, 1),
    BIRCH_LOG(Material.BIRCH_LOG, 2, 2, 1),
    JUNGLE_LOG(Material.JUNGLE_LOG, 2, 3, 1),
    ACACIA_LOG(Material.ACACIA_LOG, 2, 4, 1),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, 2, 5, 1),
    GLASS_PANE(Material.GLASS_PANE, 2, 6, 1),
    CRIMSON_STEM(Material.CRIMSON_STEM, 2, 0, 2),
    WARPED_STEM(Material.WARPED_STEM, 2, 1, 2),
    NETHERRACK(Material.NETHERRACK, 2, 2, 2),
    SOUL_SAND(Material.SOUL_SAND, 2, 3, 2),
    SOUL_SOIL(Material.SOUL_SOIL, 2, 4, 2),
    BASALT(Material.BASALT, 2, 5, 2),
    WHITE_STAINED_GLASS(Material.WHITE_STAINED_GLASS, 2, 6, 2),
    BLACKSTONE(Material.BLACKSTONE, 2, 0, 3),
    MAGMA_BLOCK(Material.MAGMA_BLOCK, 2, 1, 3),
    SHROOMLIGHT(Material.SHROOMLIGHT, 2, 2, 3),
    TERRACOTTA(Material.TERRACOTTA, 2, 3, 3),
    END_STONE(Material.END_STONE, 2, 4, 3),
    GRAVEL(Material.GRAVEL, 2, 5, 3),
    WHITE_STAINED_GLASS_PANE(Material.WHITE_STAINED_GLASS_PANE, 2, 6, 3),
    SNOW_BLOCK(Material.SNOW_BLOCK, 2, 0, 4),
    ICE(Material.ICE, 2, 1, 4),
    PACKED_ICE(Material.PACKED_ICE, 2, 2, 4),
    BLUE_ICE(Material.BLUE_ICE, 2, 3, 4),
    PRISMARINE(Material.PRISMARINE, 2, 4, 4),
    SEA_LANTERN(Material.SEA_LANTERN, 2, 5, 4),
    TINTED_GLASS(Material.TINTED_GLASS, 2, 6, 4),

    // MOB DROPS

    ROTTEN_FLESH(Material.ROTTEN_FLESH, 3, 0, 0),
    STRING(Material.STRING, 3, 1, 0),
    SPIDER_EYE(Material.SPIDER_EYE, 3, 2, 0),
    GUNPOWDER(Material.GUNPOWDER, 3, 3, 0),
    BONE(Material.BONE, 3, 4, 0),
    SLIME_BALL(Material.SLIME_BALL, 3, 5, 0),
    MAGMA_CREAM(Material.MAGMA_CREAM, 3, 0, 1),
    BLAZE_ROD(Material.BLAZE_ROD, 3, 1, 1),
    GHAST_TEAR(Material.GHAST_TEAR, 3, 2, 1),
    ENDER_PEARL(Material.ENDER_PEARL, 3, 3, 1),
    DRAGON_BREATH(Material.DRAGON_BREATH, 3, 4, 1),
    PHANTOM_MEMBRANE(Material.PHANTOM_MEMBRANE, 3, 5, 1),
    INK_SAC(Material.INK_SAC, 3, 0, 2),
    GLOW_INK_SAC(Material.GLOW_INK_SAC, 3, 1, 2),
    LEATHER(Material.LEATHER, 3, 2, 2),
    RABBIT_HIDE(Material.RABBIT_HIDE, 3, 3, 2),
    RABBIT_FOOT(Material.RABBIT_FOOT, 3, 4, 2),
    FEATHER(Material.FEATHER, 3, 5, 2),
    WHITE_WOOL(Material.WHITE_WOOL, 3, 0, 3),
    BONE_BLOCK(Material.BONE_BLOCK, 3, 1, 3),
    SHULKER_SHELL(Material.SHULKER_SHELL, 3, 2, 3),
    SHULKER_BOX(Material.SHULKER_BOX, 3, 3, 3),

    // MISCELLANEOUS

    FLINT(Material.FLINT, 4, 0, 0),
    SNOWBALL(Material.SNOWBALL, 4, 1, 0),
    CLAY_BALL(Material.CLAY_BALL, 4, 2, 0),
    ARROW(Material.ARROW, 4, 3, 0),
    NETHER_WART(Material.NETHER_WART, 4, 4, 0),
    EXPERIENCE_BOTTLE(Material.EXPERIENCE_BOTTLE, 4, 5, 0),
    PRISMARINE_SHARD(Material.PRISMARINE_SHARD, 4, 0, 1),
    PRISMARINE_CRYSTALS(Material.PRISMARINE_CRYSTALS, 4, 1, 1),
    NAUTILUS_SHELL(Material.NAUTILUS_SHELL, 4, 2, 1),
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA, 4, 3, 1),
    SPONGE(Material.SPONGE, 4, 4, 1),
    NAME_TAG(Material.NAME_TAG, 4, 5, 1),
    CHORUS_FRUIT(Material.CHORUS_FRUIT, 4, 0, 2),
    CHORUS_FLOWER(Material.CHORUS_FLOWER, 4, 1, 2),
    SCUTE(Material.SCUTE, 4, 2, 2),
    LEAD(Material.LEAD, 4, 3, 2),
    TURTLE_EGG(Material.TURTLE_EGG, 4, 4, 2),
    NETHER_STAR(Material.NETHER_STAR, 4, 5, 2);

    private static final ProtoBusiness instance = ProtoBusiness.getInstance();

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
     * Gets formatted name of the GUI item
     * @return formatted name
     */
    public String getName() {
        return WordUtils.capitalize(name().toLowerCase().replace("_", " "));
    }

    /**
     * Gets the OrderBook of the GUI item
     * @return OrderBook
     */
    public OrderBook getBook() {
        for (OrderBook book : instance.getMarketManager().getBooks().values()) {
            if (book.getItem() == this) return book;
        }
        return null;
    }

    /**
     * Gets the items of a specific page
     * @param page page integer (starts at 0)
     * @return a list of all the items in given page
     */
    public static List<OrderItem> getItemsOfPage(int page) {
        List<OrderItem> list = new ArrayList<>();
        for (OrderItem item : OrderItem.values()) {
            if (item.getPage() == page) list.add(item);
        }
        return list;
    }

    public static OrderItem getFromMaterial(Material mat) {
        for (OrderItem i : OrderItem.values()) {
            if (i.getMaterial() == mat) return i;
        }
        return null;
    }

}
