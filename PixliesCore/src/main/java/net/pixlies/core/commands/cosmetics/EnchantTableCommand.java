package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("enchanttable|enchantmenttable|etable")
@CommandPermission("pixlies.cosmetics.enchantmenttable")
public class EnchantTableCommand extends BaseCommand {

    @Default
    @Description("Repairs items")
    public void onRepair(Player player) {
        player.openEnchanting(null, true);
    }

}
