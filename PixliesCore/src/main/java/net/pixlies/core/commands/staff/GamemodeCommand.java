package net.pixlies.core.commands.staff;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.TextUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm|monkeymode|mm")
@Description("Change your gamemode")
@CommandPermission("pixlies.staff.gamemode")
public class GamemodeCommand extends BaseCommand {

    @CommandAlias("gmc")
    @Subcommand("creative|c|1")
    @CommandPermission("pixlies.staff.gamemode.creative")
    @Description("Change your gamemode to creative")
    @CommandCompletion("@players")
    public void onCreative(Player player, @Optional Player target) {
        changeGameMode(player, target, GameMode.CREATIVE);
    }

    @CommandAlias("gms")
    @Subcommand("survival|s|0")
    @CommandPermission("pixlies.staff.gamemode.survival")
    @Description("Change your gamemode to survival")
    @CommandCompletion("@players")
    public void onSurvival(Player player, @Optional Player target) {
        changeGameMode(player, target, GameMode.SURVIVAL);
    }

    @CommandAlias("gma")
    @Subcommand("adventure|a|2")
    @CommandPermission("pixlies.staff.gamemode.adventure")
    @Description("Change your gamemode to adventure")
    @CommandCompletion("@players")
    public void onAdventure(Player player, @Optional Player target) {
        changeGameMode(player, target, GameMode.ADVENTURE);
    }

    @CommandAlias("gmsp")
    @Subcommand("spectator|sp|3")
    @CommandPermission("pixlies.staff.gamemode.spectator")
    @Description("Change your gamemode to spectator")
    @CommandCompletion("@players")
    public void onSpectator(Player player, @Optional Player target) {
        changeGameMode(player, target, GameMode.SPECTATOR);
    }



    public void changeGameMode(Player player, Player target, GameMode gameMode) {
        if (target == null) {
            player.setGameMode(gameMode);
            Lang.STAFF_GAMEMODE_CHANGED_SELF.send(player, "%GAMEMODE%;" + TextUtils.getGameModeFormatted(gameMode));
        } else {
            target.setGameMode(gameMode);
            Lang.STAFF_GAMEMODE_CHANGED_OTHERS.send(player, "%GAMEMODE%;" + TextUtils.getGameModeFormatted(gameMode), "%TARGET%;" + target.getName(), "%CHANGER%;" + player.getName());
            Lang.STAFF_GAMEMODE_CHANGED_OTHERS.send(target, "%GAMEMODE%;" + TextUtils.getGameModeFormatted(gameMode), "%TARGET%;" + target.getName(), "%CHANGER%;" + player.getName());
        }
    }

}
