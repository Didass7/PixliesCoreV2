package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("smithing|smithingtable|smith|mrsmith")
@CommandPermission("pixlies.cosmetics.smithing")
public class SmithingTableCommand extends BaseCommand {

    @Default
    @Description("Smith")
    public void onSmith(Player player) {
        player.openSmithingTable(null, false);
    }

}
