package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("lock")
@CommandPermission("pixlies.player.lock")
public class LockCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default
    @Description("Locks/unlocks a chest")
    public void onLock(Player sender) {
        User user = User.get(sender.getUniqueId());

        Block block = sender.getTargetBlock(5);
        if (block == null || !block.getType().equals(Material.CHEST)) {
            Lang.MUST_BE_A_PLAYER.send(sender);
            return;
        }

        TileState state = (TileState) block.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(instance, "locked-chests");
        String ownerUuid = container.getOrDefault(key, PersistentDataType.STRING, "none");

        // If chest already is locked
        if (container.has(key, PersistentDataType.STRING)) {
            // If owner is not the sender AND if sender is not in staff mode
            if (!ownerUuid.equals(sender.getUniqueId().toString()) && !user.isStaffModeEnabled()) {
                Lang.CHEST_BELONGS_TO_OTHER.send(sender, "%PLAYER%;" + ownerUuid);
            } else {
                container.set(key, PersistentDataType.STRING, "none");
                Lang.CHEST_UNLOCKED.send(sender);
                sender.playSound(sender.getLocation(), "entity.arrow.hit_player", 100, 1);
            }
        } else {
            container.set(key, PersistentDataType.STRING, sender.getUniqueId().toString());
            Lang.CHEST_LOCKED.send(sender);
            sender.playSound(sender.getLocation(), "block.chest.locked", 100, 1);
        }

        state.update();
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

}
