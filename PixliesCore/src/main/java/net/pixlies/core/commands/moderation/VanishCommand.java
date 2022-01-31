package net.pixlies.core.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("vanish|v")
@CommandPermission("pixlies.moderation.vanish")
public class VanishCommand extends BaseCommand {

    private final VanishHandler handler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    @CommandCompletion("@empty")
    public void onVanish(CommandSender sender, Player player) {
        // TODO
    }

    @Private
    @Default
    @CommandCompletion("@empty")
    public void onVanish(Player player) {

    }

}
