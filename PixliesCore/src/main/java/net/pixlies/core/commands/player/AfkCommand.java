package net.pixlies.core.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.pixlies.core.localization.Lang;
import org.bukkit.entity.Player;

/**
 * AFK Command
 * @author dynmie
 */
public class AfkCommand extends BaseCommand {

    @Default
    @CommandAlias("afk")
    public void onAfk(Player player) {

        if (player.isAfk()) {
            Lang.PLAYER_AFK_OFF.send(player);
        } else {
            Lang.PLAYER_AFK_ON.send(player);
        }

        player.setAfk(!player.isAfk());

    }

}
