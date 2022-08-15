package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

@CommandAlias("suicide")
@CommandPermission("pixlies.player.suicide")
public class SuicideCommand extends BaseCommand {

    @Default
    @Description("Commit die")
    public void onSuicide(Player player) {
        player.setHealth(0);
        Lang.PLAYER_SUICIDE.send(player);
    }

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

}
