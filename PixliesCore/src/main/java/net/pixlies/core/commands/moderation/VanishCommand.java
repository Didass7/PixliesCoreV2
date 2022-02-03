package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("vanish|v")
@CommandPermission("pixlies.moderation.vanish")
public class VanishCommand extends BaseCommand {

    private final VanishHandler handler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    @CommandCompletion("@players")
    public void onVanish(CommandSender sender, Player target) {
        User user = User.get(target.getUniqueId());
        boolean vanished = user.getSettings().isVanished();
        handler.setVanished(target, !vanished);
        if (!vanished) {
            Lang.MODERATION_VANISH_ON_OTHER.send(sender, "%PLAYER%;" + target.getName());
        } else {
            Lang.MODERATION_VANISH_OFF_OTHER.send(sender, "%PLAYER%;" + target.getName());
        }
    }

    @Private
    @Default
    public void onVanish(Player player) {
        User user = User.get(player.getUniqueId());
        boolean vanished = user.getSettings().isVanished();
        handler.setVanished(player, !vanished);
        if (!vanished) {
            Lang.MODERATION_VANISH_ON.send(player);
        } else {
            Lang.MODERATION_VANISH_OFF.send(player);
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }


}
