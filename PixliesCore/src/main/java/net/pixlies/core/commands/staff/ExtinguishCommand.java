package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;

/**
 * Extinguish a player
 * @author dynmie
 */
@CommandPermission("pixlies.staff.extinguish")
public class ExtinguishCommand extends BaseCommand {

    @Default
    @CommandAlias("extinguish|ext")
    @Description("Extinguish a player")
    public void onExtinguish(Player player, @Optional Player target) {

        if (target != null) {
            target.setFireTicks(0);
            return;
        }

        player.setFireTicks(0);

    }

}
