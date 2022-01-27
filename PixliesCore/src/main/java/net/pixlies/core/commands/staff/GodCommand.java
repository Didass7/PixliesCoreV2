package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("god")
@CommandPermission("pixlies.staff.god")
public class GodCommand extends BaseCommand {

    @Default
    @Private
    public void onGod(Player player) {
        player.setInvulnerable(!player.isInvulnerable());
        if (player.isInvulnerable()) {
            Lang.MODERATION_GOD_ON.send(player);
        } else {
            Lang.MODERATION_GOD_OFF.send(player);
        }
    }

    @CommandCompletion("@players")
    @CommandPermission("pixlies.staff.god.others")
    public void onGod(CommandSender sender, Player player) {
        player.setInvulnerable(!player.isInvulnerable());
        if (player.isInvulnerable()) {
            Lang.MODERATION_GOD_ON.send(sender);
        } else {
            Lang.MODERATION_GOD_OFF.send(sender);
        }
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
