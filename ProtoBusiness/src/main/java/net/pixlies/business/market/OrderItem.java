package net.pixlies.business.market;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
public enum OrderItem {

    // TODO: List all items which can be traded (with their prices)

    DIAMOND(Material.DIAMOND, 10),
    GOLD_INGOT(Material.GOLD_INGOT, 5),
    IRON_INGOT(Material.IRON_INGOT, 1),
    EMERALD(Material.EMERALD, 50);

    @Getter private final Material material;
    @Getter private final double originalPrice;

}
