package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

@CommandAlias("sit")
@CommandPermission("pixlies.cosmetics.sit")
public class SitCommand extends BaseCommand {

    private final Main instance = Main.getInstance();

    @Default
    @Description("You can sit")
    public void onSit(Player player) {

        Location location = player.getLocation();
        Block relative = location.getBlock().getRelative(BlockFace.DOWN);

        User user = User.get(player.getUniqueId());

        if (relative.getType() == Material.AIR || player.isInWater() || player.isFrozen() || user.getSettings().isPassive()) {
            Lang.COSMETICS_CANNOT_SIT_HERE.send(player);
            return;
        }

        Arrow arrow = player.getWorld().spawnArrow(relative.getLocation(), new Vector(0, 0, 0), 0, 0);

        NamespacedKey key = new NamespacedKey(instance, "chair");
        PersistentDataContainer container = arrow.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, player.getUniqueId().toString());

        arrow.setInvulnerable(true);
        arrow.setGravity(false);
        arrow.setCritical(false);
        arrow.setDamage(0);
        arrow.addPassenger(player);

    }

}
