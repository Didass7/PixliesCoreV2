package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
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
        handler.setVanished(target, !handler.isVanished(target.getUniqueId()));
        if (handler.isVanished(target.getUniqueId())) {
            Lang.MODERATION_VANISH_ON_OTHER.send(sender, "%PLAYER%;" + target.getName());
        } else {
            Lang.MODERATION_VANISH_OFF_OTHER.send(sender, "%PLAYER%;" + target.getName());
        }
    }

    @Private
    @Default
    @CommandCompletion("@empty")
    public void onVanish(Player player) {
        handler.setVanished(player, !handler.isVanished(player.getUniqueId()));
        if (handler.isVanished(player.getUniqueId())) {
            Lang.MODERATION_VANISH_ON.send(player);
        } else {
            Lang.MODERATION_VANISH_OFF.send(player);
        }
    }

}
