package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("stonecutter|stonecut|cutstone|cutastone")
@CommandPermission("pixlies.cosmetics.stonecutter")
public class StoneCutterCommand extends BaseCommand {

    @Default
    @Description("Cut stone")
    public void onRepair(Player player) {
        player.openStonecutter(null, true);
    }

}
