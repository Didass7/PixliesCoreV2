package net.pixlies.core.handlers.impl.staffmode;

import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.impl.moderation.StaffModeStatusChangeEvent;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.handlers.impl.PassiveHandler;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.handlers.impl.staffmode.data.PreviousInventory;
import net.pixlies.core.utils.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StaffModeHandler implements Handler {

    private static final Main instance = Main.getInstance();
    private final VanishHandler vanishHandler = instance.getHandlerManager().getHandler(VanishHandler.class);
    private final PassiveHandler passiveHandler = instance.getHandlerManager().getHandler(PassiveHandler.class);

    private final Config config = instance.getStaffModeConfig();

    public void enable(Player player) {
        User user = User.get(player.getUniqueId());
        if (EventUtils.callCancelable(new StaffModeStatusChangeEvent(player, user, StaffModeStatusChangeEvent.StaffModeStatus.ENABLE)).isCancelled()) return;
        if (user.getSettings().isStaffModeEnabled()) return;
        enableWithoutUpdate(player, user);
        user.getSettings().setStaffModeEnabled(true);
    }

    public void disable(Player player) {
        User user = User.get(player.getUniqueId());
        if (EventUtils.callCancelable(new StaffModeStatusChangeEvent(player, user, StaffModeStatusChangeEvent.StaffModeStatus.DISABLE)).isCancelled()) return;
        if (!user.getSettings().isStaffModeEnabled()) return;
        disableWithoutUpdate(player, user);
        user.getSettings().setStaffModeEnabled(false);
    }

    public void enableWithoutUpdate(Player player, User user) {
        passiveHandler.setPassive(player, true);
        PlayerUtils.heal(player);
        saveItems(player);
        loadStaffItems(player, user);
        passiveHandler.setPassive(player, true);
    }

    public void disableWithoutUpdate(Player player, User user) {
        if (!user.getSettings().isVanished()) {
            passiveHandler.setPassive(player, false);
        }
        loadItems(player);
    }

    private void loadStaffItems(Player player, User user) {

        boolean vanished = user.getSettings().isVanished();
        String vanishName = vanished ? "&3Vanish &8[&aON&8]" : "&3Vanish &8[&cOFF&8]";
        Material vanishMaterial = vanished ? Material.LIME_DYE : Material.GRAY_DYE;

        // ITEMS
        ItemStack invsee = new ItemBuilder(Material.BOOK).setDisplayName(CC.format("&3Compass")).build();
        ItemStack freeze = new ItemBuilder(Material.PACKED_ICE).setDisplayName(CC.format("&3Freeze Player")).build();
        ItemStack compass = new ItemBuilder(Material.COMPASS).setDisplayName(CC.format("&3Teleport")).build();
        ItemStack randomTeleporter = new ItemBuilder(Material.CLOCK).setDisplayName(CC.format("&3Random Teleport")).build();
        ItemStack vanish = new ItemBuilder(vanishMaterial).setDisplayName(CC.format(vanishName)).build();

        // LOAD INVENTORY
        Inventory inventory =  player.getInventory();

        inventory.setItem(0, invsee);
        inventory.setItem(3, freeze);
        inventory.setItem(4, compass);
        inventory.setItem(5, randomTeleporter);
        inventory.setItem(8, vanish);

        player.setGameMode(GameMode.CREATIVE);

    }

    private void saveItems(Player player) {

        PreviousInventory prevInv = new PreviousInventory(
                player.getTotalExperience(),
                player.getInventory().getArmorContents(),
                player.getInventory().getContents(),
                player.getGameMode()
        );

        prevInv.saveToConfig(config, player.getUniqueId().toString());

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

    }

    private void loadItems(Player player) {

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        UUID uuid = player.getUniqueId();
        PreviousInventory prevInv = PreviousInventory.fromConfig(config, uuid.toString());

        // Set items
        player.getInventory().setContents(prevInv.getInventoryContents());
        player.getInventory().setArmorContents(prevInv.getArmorContents());
        player.setTotalExperience(prevInv.getXp());
        player.setGameMode(prevInv.getGameMode());

    }

}
