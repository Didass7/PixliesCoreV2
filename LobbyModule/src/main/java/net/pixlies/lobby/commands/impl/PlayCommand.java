package net.pixlies.lobby.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.QueueManager;
import org.bukkit.entity.Player;

public class PlayCommand extends BaseCommand {

    private static final QueueManager manager = Lobby.getInstance().getQueueManager();

    @Default
    @CommandAlias("play|join|queue|joinserver")
    @CommandCompletion("@empty")
    @Syntax("<server>")
    public void onPlay(Player player, @Single String server) {

        if (manager.isInQueue(player)) {
            player.sendMessage(Lang.PIXLIES + CC.format("&7You can only be in one queue at a time. Use &d/leavequeue&7 to leave your current queue."));
            return;
        }

        manager.addPlayerToQueue(player, server);

    }

}


