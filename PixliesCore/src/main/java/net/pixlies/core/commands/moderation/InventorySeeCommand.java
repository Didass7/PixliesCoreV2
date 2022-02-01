package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("invsee|inventorysee|isee")
@CommandPermission("pixlies.moderation.invsee")
public class InventorySeeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@empty")
    @Syntax("<target>")
    public void onInventorySee(Player player, Player target) {
        if (!player.hasPermission("pixlies.moderation.invsee.self")) {
            Lang.CANNOT_USE_ON_SELF.send(player);
            return;
        }
        player.openInventory(target.getInventory());
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
