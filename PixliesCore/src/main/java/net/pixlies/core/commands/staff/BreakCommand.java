package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.pixlies.core.localization.Lang;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Break command.
 * Breaks the block you are currently looking at in a 5 block radius.
 * @author dynmie
 */
@CommandPermission("pixlies.staff.break")
public class BreakCommand extends BaseCommand {

    @Default
    @CommandAlias("break")
    @Description("Breaks the block you are looking at")
    public void onBreak(Player player) {

        Block block = player.getTargetBlock(5);

        if (block == null) {
            Lang.STAFF_BREAK_BLOCK_NOT_FOUND.send(player);
            return;
        }

        block.setType(Material.AIR);
        Lang.STAFF_BREAK_DONE.send(player);

    }

}
