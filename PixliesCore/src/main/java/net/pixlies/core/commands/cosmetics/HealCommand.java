package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("heal|hl")
@CommandPermission("pixlies.cosmetics.heal")
public class HealCommand extends BaseCommand {

    @Default
    public void onHeal(Player player) {
        PlayerUtils.heal(player);
        Lang.COSMETICS_HEALED_SELF.send(player);
    }

    @CommandPermission("pixlies.cosmetics.heal.others")
    @CommandCompletion("@players")
    public void onHeal(CommandSender sender, Player player) {
        PlayerUtils.heal(player);
        Lang.COSMETICS_HEALED_OTHERS.send(sender, "%PLAYER%;" + player.getName());
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
