package net.pixlies.core.commands.cosmetics;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.CosmeticsHandler;
import net.pixlies.core.localization.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("suicide")
@CommandPermission("pixlies.cosmetics.suicide")
public class SuicideCommand extends BaseCommand {

    private static final Main instance = Main.getInstance();
    private final CosmeticsHandler cosmeticsHandler = instance.getHandlerManager().getHandler(CosmeticsHandler.class);

    @Default
    @Description("Kills the player")
    public void onSuicide(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            Lang.MUST_BE_A_PLAYER.send(sender);
        } else {
            Lang.COSMETICS_PLAYER_SUICIDE.send(player);
            cosmeticsHandler.setSuicidalDeath(true);
            player.setHealth(0);
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    public SuicideCommand() {}

}
