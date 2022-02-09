package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

/**
 * Open a loom
 * @author dynmie
 */
@CommandPermission("pixlies.cosmetics.loom")
public class LoomCommand extends BaseCommand {

    @Default
    @CommandAlias("loom")
    @Description("Open a loom")
    public void onLoom(Player player, @Optional Player target) {
        if (target != null) {
            target.openLoom(null, true);
            return;
        }
        player.openLoom(null, true);
    }

}
