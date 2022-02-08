package net.pixlies.nations.commands.impl.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("lock")
@CommandPermission("pixlies.player.lock")
public class LockCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();

    @Default // TODO: Staff bypass
    @Description("Locks/unlocks a chest")
    public void onLock(Player player) {

        User user = User.get(player.getUniqueId());

        Block block = player.getTargetBlock(5);
        if (block == null || block.getType() != Material.CHEST || !(block.getState() instanceof TileState state)) {
            Lang.MUST_BE_A_PLAYER.send(player);
            return;
        }

        PersistentDataContainer container = state.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(instance, "nations:locked-chests");
        String ownerUuid = container.getOrDefault(key, PersistentDataType.STRING, "none");

        if (container.has(key, PersistentDataType.PrimitivePersistentDataType.STRING)) {

            if (!ownerUuid.equals(player.getUniqueId().toString())) {
                Lang.CHEST_BELONGS_TO_OTHER.send(player, "%PLAYER%;" + user.getNickName());
                return;
            }

            container.remove(key);
            player.playSound(player.getLocation(), "entity.arrow.hit_player", Float.MAX_VALUE, 1);
            Lang.CHEST_UNLOCKED.send(player);

            state.update();
            return;

        }

        container.set(key, PersistentDataType.PrimitivePersistentDataType.STRING, player.getUniqueId().toString());
        Lang.CHEST_LOCKED.send(player);
        player.playSound(player.getLocation(), "block.chest.locked", Float.MAX_VALUE, 1);

        state.update();

    }

}
