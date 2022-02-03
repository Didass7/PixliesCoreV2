package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

public class AfkCommand extends BaseCommand {

    @CommandAlias("afk")
    public void onAfk(Player player) {
        player.setAfk(!player.isAfk());
        if (player.isAfk()) {
            Lang.PLAYER_AFK_ON.send(player);
        } else {
            Lang.PLAYER_AFK_OFF.send(player);
        }
    }

}
