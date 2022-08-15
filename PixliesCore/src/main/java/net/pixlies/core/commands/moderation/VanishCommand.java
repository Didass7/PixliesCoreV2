package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand extends BaseCommand {

    private final VanishHandler handler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    @CommandAlias("vanish|v")
    @CommandCompletion("@players")
    @CommandPermission("pixlies.moderation.vanish")
    public void onVanish(CommandSender sender, @Optional @Single String starget) {

        if (starget == null) {
            if (!(sender instanceof Player player)) {
                throw new InvalidCommandArgument();
            }

            User user = User.get(player.getUniqueId());
            boolean vanished = user.isVanished();
            if (vanished) {
                handler.setVanished(player, false);
                Lang.MODERATION_VANISH_OFF.send(player);
            } else {
                handler.setVanished(player, true);
                Lang.MODERATION_VANISH_ON.send(player);
            }
            return;
        }

        if (!sender.hasPermission("pixlies.moderation.vanish.other")) {
            throw new ConditionFailedException(MessageKeys.PERMISSION_DENIED_PARAMETER);
        }

        Player target = Bukkit.getPlayer(starget);
        if (target == null) {
            throw new ConditionFailedException(MessageKeys.COULD_NOT_FIND_PLAYER, "{search}", starget);
        }

        User user = User.get(target.getUniqueId());
        boolean vanished = user.isVanished();
        if (vanished) {
            handler.setVanished(target, false);
            Lang.MODERATION_VANISH_OFF_OTHER.send(sender, "%PLAYER%;" + target.getName());
        } else {
            handler.setVanished(target, true);
            Lang.MODERATION_VANISH_ON_OTHER.send(sender, "%PLAYER%;" + target.getName());
        }

    }

}
