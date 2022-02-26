package net.pixlies.core.handlers.impl.staffmode;

import net.pixlies.core.Main;
import net.pixlies.core.configuration.Config;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.impl.moderation.StaffModeStatusChangeEvent;
import net.pixlies.core.handlers.Handler;
import net.pixlies.core.handlers.impl.PassiveHandler;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.handlers.impl.staffmode.data.PreviousInventory;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.EventUtils;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
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
        if (user.getSettings().isInStaffMode()) return;
        enableWithoutUpdate(player, user);
        user.getSettings().setInStaffMode(true);
        user.save();
    }

    public void disable(Player player) {
        User user = User.get(player.getUniqueId());
        if (EventUtils.callCancelable(new StaffModeStatusChangeEvent(player, user, StaffModeStatusChangeEvent.StaffModeStatus.DISABLE)).isCancelled()) return;
        if (!user.getSettings().isInStaffMode()) return;
        disableWithoutUpdate(player, user);
        user.getSettings().setInStaffMode(false);
        user.save();
    }

    public void enableWithoutUpdate(Player player, User user) {
        passiveHandler.setPassive(player, true);
        PlayerUtils.heal(player);
        saveItems(player);
        loadStaffItems(player, user);
    }

    public void disableWithoutUpdate(Player player, User user) {
        if (!user.getSettings().isVanished()) {
            passiveHandler.setPassive(player, false);
        }
        loadItems(player);
    }

    private void loadStaffItems(Player player, User user) {

        // ITEMS
        ItemStack invsee = getInspectItem();
        ItemStack freeze = getFreezeItem();
        ItemStack compass = getTeleportItem();
        ItemStack randomTeleporter = getRandomTeleportItem();
        ItemStack vanish = getVanishItem(user.getSettings().isVanished());

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

        // SET PREVIOUS INVENTORY
        PreviousInventory prevInv = new PreviousInventory(
                player.getTotalExperience(),
                player.getInventory().getArmorContents(),
                player.getInventory().getContents(),
                player.getGameMode()
        );

        // SAVE TO CONFIG
        prevInv.saveToConfig(config, player.getUniqueId().toString());

        // CLEAR INVENTORY
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setTotalExperience(0);

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

    public static ItemStack getInspectItem() {
        return new ItemBuilder(Material.BOOK)
                .setDisplayName(CC.format("&bInspect Inventory"))
                .addLoreLine(CC.format("&7Left click on a player to inspect."))
                .build();
    }

    public static ItemStack getFreezeItem() {
        return new ItemBuilder(Material.PACKED_ICE)
                .setDisplayName(CC.format("&bFreeze Player"))
                .addLoreLine(CC.format("&7Left click on a player to freeze."))
                .build();
    }

    public static ItemStack getTeleportItem() {
        return new ItemBuilder(Material.COMPASS)
                .setDisplayName(CC.format("&bTeleport"))
                .addLoreLine(CC.format("&7Left click to teleport to a block."))
                .addLoreLine(CC.format("&7Right click to jump through walls."))
                .build();
    }

    public static ItemStack getRandomTeleportItem() {
        return new ItemBuilder(Material.CLOCK)
                .setDisplayName(CC.format("&bRandom Teleport"))
                .addLoreLine(CC.format("&7Left click to random teleport."))
                .build();
    }

    public static ItemStack getVanishItem(boolean vanish) {
        Material material = vanish ? Material.LIME_DYE : Material.GRAY_DYE;
        String displayName = vanish ? CC.format("&aVanish") : CC.format("&7Vanish");
        return new ItemBuilder(material)
                .setDisplayName(CC.format(displayName))
                .addLoreLine(CC.format("&7Left click to toggle."))
                .build();
    }

}
