package net.pixlies.core.handlers.impl;

import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.impl.moderation.StaffModeStatusChangeEvent;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.utils.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class StaffModeHandler implements Handler {

    private static final Main instance = Main.getInstance();
    private final VanishHandler vanishHandler = instance.getHandlerManager().getHandler(VanishHandler.class);
    private final PassiveHandler passiveHandler = instance.getHandlerManager().getHandler(PassiveHandler.class);

    private final Config config = instance.getStaffModeConfig();

    public void enable(Player player) {
        User user = User.get(player.getUniqueId());
        if (EventUtils.callCancelable(new StaffModeStatusChangeEvent(player, user)).isCancelled()) return;
        user.getSettings().setStaffModeEnabled(true);
        PlayerUtils.heal(player);
        saveItems(player);
        loadStaffItems(player);
        passiveHandler.setPassive(player, true);
    }

    public void disable(Player player) {
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) {
            passiveHandler.setPassive(player, false);
        }
        loadItems(player);
        user.getSettings().setStaffModeEnabled(false);
    }

    private void loadStaffItems(Player player) {
//        ItemStack compass = new ItemBuilder(Material.COMPASS).setDisplayName(CC.format("&3Compass")).build();
//        ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(CC.format("&3Compass")).build();
//        ItemStack freeze = new ItemBuilder(Material.PACKED_ICE).setDisplayName(CC.format("&3Compass")).build();
//        ItemStack randomTeleporter = new ItemBuilder(Material.CLOCK).setDisplayName(CC.format("&3Compass")).build();
//        ItemStack invsee = new ItemBuilder(Material.BOOK).setDisplayName(CC.format("&3Compass")).build();
//        ItemStack vanish = new ItemBuilder(Material.LIME_DYE).setDisplayName(CC.format("&3Compass")).build();
    }

    private void saveItems(Player player) {

        // TODO save

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItemInOffHand(null);

        player.setTotalExperience(0);

    }

    private void loadItems(Player player) {

        String uuid = player.getUniqueId().toString();

        // Clear inv
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItemInOffHand(null);

        // Get items
        String inventory = config.getString(uuid + ".inventory");
        int xp = config.getInt(uuid + ".xp");

        // Set items
//        InventoryUtils.(inventory, player.getInventory());
        player.setTotalExperience(xp);

    }

}
