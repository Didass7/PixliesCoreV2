package net.pixlies.core.handlers.impl.staffmode.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.utils.InventoryUtils;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class PreviousInventory {

    private int xp;
    private ItemStack[] armorContents;
    private ItemStack[] inventoryContents;
    private GameMode gameMode;

    public static PreviousInventory fromConfig(Config config, String key) {

        return new PreviousInventory(
                config.getInt(key + ".xp"),
                InventoryUtils.itemStackArrayFromBase64(config.getString(key + ".armor")),
                InventoryUtils.itemStackArrayFromBase64(config.getString(key + ".inventory")),
                GameMode.valueOf(config.getString(key + ".gamemode"))
        );

    }

    public void saveToConfig(Config config, String key) {
        config.set(key + ".xp", xp);
        config.set(key + ".armor", InventoryUtils.itemStackArrayToBase64(armorContents));
        config.set(key + ".inventory", InventoryUtils.itemStackArrayToBase64(inventoryContents));
        config.set(key + ".gamemode", gameMode.toString());
        config.save();
    }

}
