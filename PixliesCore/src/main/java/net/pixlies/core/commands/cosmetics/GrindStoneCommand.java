package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("grindstone|grind")
@CommandPermission("pixlies.cosmetics.grindstone")
public class GrindStoneCommand extends BaseCommand {

    @Default
    @Description("Grind items")
    public void onGrind(Player player) {
        player.openGrindstone(null, true);
    }

}
